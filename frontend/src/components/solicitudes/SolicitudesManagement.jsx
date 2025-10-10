import { useState, useEffect } from "react";
import { Plus, Trash2, Send, RefreshCw, ArrowRight } from "lucide-react";

import { useAuth } from "../../contexts/AuthContext";

import Button from "../ui/Button";
import Modal from "../ui/Modal";
import Input from "../ui/Input";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableData,
} from "../ui/Table";

const API_BASE_URL = "http://localhost:8080";

const SolicitudesManagement = () => {
  const { user } = useAuth();

  // Estados para datos
  const [solicitudesExternas, setSolicitudesExternas] = useState([]);
  const [ofertasExternas, setOfertasExternas] = useState([]);
  const [transferenciasRecibidas, setTransferenciasRecibidas] = useState([]);

  // Estados para UI
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState("solicitudes");

  // Estados para modales
  const [modalCrearSolicitud, setModalCrearSolicitud] = useState(false);
  const [modalCrearOferta, setModalCrearOferta] = useState(false);
  const [modalDarBaja, setModalDarBaja] = useState(false);
  const [modalTransferir, setModalTransferir] = useState(false);

  useEffect(() => {
    if (activeTab === "solicitudes") {
      cargarSolicitudesExternas();
    } else if (activeTab === "ofertas") {
      cargarOfertasExternas();
    } else if (activeTab === "transferencias") {
      cargarTransferenciasRecibidas();
    }
  }, [activeTab]);

  // Estados para formulario de crear solicitud
  const [formSolicitud, setFormSolicitud] = useState({
    idSolicitud: "", // Solo pedir el ID de solicitud, el de org viene del usuario
    donaciones: [{ categoria: "", descripcion: "" }],
  });

  // Estados para formulario de crear oferta
  const [formOferta, setFormOferta] = useState({
    idOferta: "", // Solo pedir el ID de oferta, el de org viene del usuario
    donaciones: [{ categoria: "", descripcion: "", cantidad: "" }],
  });

  // Estados para formulario de baja
  const [formBaja, setFormBaja] = useState({
    idSolicitud: "", // Solo pedir el ID de solicitud, el de org viene del usuario
  });

  // Estados para formulario de transferencia
  const [solicitudSeleccionada, setSolicitudSeleccionada] = useState(null);
  const [formTransferencia, setFormTransferencia] = useState({
    idOrganizacionDonante: "",
    donaciones: [{ categoria: "", descripcion: "", cantidad: "" }],
  });

  // Verificar permisos
  if (
    !user ||
    !["PRESIDENTE", "VOCAL", "COORDINADOR", "VOLUNTARIO"].includes(user.role)
  ) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
        No tenés permisos para acceder a esta sección.
      </div>
    );
  }

  // ============================================
  // FUNCIONES DE CARGA DE DATOS
  // ============================================

  const cargarSolicitudesExternas = async () => {
    setLoading(true);
    setError(null);
    try {
      // Llamar directamente al Kafka Server que consulta la BD
      // Pasar el id_organizacion para excluir las propias solicitudes
      const url = user?.id_organizacion
        ? `http://localhost:8090/solicitudes-externas?exclude_org=${user.id_organizacion}`
        : "http://localhost:8090/solicitudes-externas";

      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setSolicitudesExternas(data.solicitudes || []);
      } else {
        throw new Error("Error al cargar solicitudes");
      }
    } catch (error) {
      console.error("Error:", error);
      setError("Error al cargar las solicitudes externas");
    } finally {
      setLoading(false);
    }
  };

  const cargarOfertasExternas = async () => {
    setLoading(true);
    setError(null);
    try {
      // Llamar directamente al Kafka Server que consulta la BD
      // Pasar el id_organizacion para excluir las propias ofertas
      const url = user?.id_organizacion
        ? `http://localhost:8090/ofertas-externas?exclude_org=${user.id_organizacion}`
        : "http://localhost:8090/ofertas-externas";

      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setOfertasExternas(data.ofertas || []);
      } else {
        throw new Error("Error al cargar ofertas");
      }
    } catch (error) {
      console.error("Error:", error);
      setError("Error al cargar las ofertas externas");
    } finally {
      setLoading(false);
    }
  };

  const cargarTransferenciasRecibidas = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(
        "http://localhost:8090/transferencias-recibidas"
      );
      if (response.ok) {
        const data = await response.json();
        setTransferenciasRecibidas(data.transferencias || []);
      } else {
        throw new Error("Error al cargar transferencias");
      }
    } catch (error) {
      console.error("Error:", error);
      setError("Error al cargar las transferencias recibidas");
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // FUNCIONES PARA CREAR SOLICITUD
  // ============================================

  const agregarDonacionSolicitud = () => {
    setFormSolicitud({
      ...formSolicitud,
      donaciones: [
        ...formSolicitud.donaciones,
        { categoria: "", descripcion: "" },
      ],
    });
  };

  const eliminarDonacionSolicitud = (index) => {
    if (formSolicitud.donaciones.length > 1) {
      setFormSolicitud({
        ...formSolicitud,
        donaciones: formSolicitud.donaciones.filter((_, i) => i !== index),
      });
    }
  };

  const actualizarDonacionSolicitud = (index, campo, valor) => {
    const nuevasDonaciones = [...formSolicitud.donaciones];
    nuevasDonaciones[index][campo] = valor;
    setFormSolicitud({ ...formSolicitud, donaciones: nuevasDonaciones });
  };

  const enviarSolicitud = async () => {
    // Validar que el usuario tenga id_organizacion
    if (!user?.id_organizacion) {
      setError("Error: No se pudo obtener la organización del usuario");
      return;
    }

    if (!formSolicitud.idSolicitud) {
      setError("Complete el ID de solicitud");
      return;
    }

    const donacionesValidas = formSolicitud.donaciones.filter(
      (d) => d.categoria.trim() && d.descripcion.trim()
    );

    if (donacionesValidas.length === 0) {
      setError("Agregue al menos una donación válida");
      return;
    }

    setLoading(true);
    setError(null);
    try {
      for (const donacion of donacionesValidas) {
        const solicitud = {
          idSolicitud: parseInt(formSolicitud.idSolicitud),
          idOrganizacion: user.id_organizacion, // Usar la org del usuario logueado
          categoria: donacion.categoria,
          descripcion: donacion.descripcion,
        };

        const response = await fetch(`${API_BASE_URL}/api/solicitudes/crear`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(solicitud),
        });

        if (!response.ok) throw new Error("Error al enviar solicitud");
      }

      setModalCrearSolicitud(false);
      setFormSolicitud({
        idSolicitud: "",
        donaciones: [{ categoria: "", descripcion: "" }],
      });
      alert("Solicitud enviada correctamente");
    } catch (error) {
      console.error("Error:", error);
      setError("Error al enviar la solicitud");
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // FUNCIONES PARA CREAR OFERTA
  // ============================================

  const agregarDonacionOferta = () => {
    setFormOferta({
      ...formOferta,
      donaciones: [
        ...formOferta.donaciones,
        { categoria: "", descripcion: "", cantidad: "" },
      ],
    });
  };

  const eliminarDonacionOferta = (index) => {
    if (formOferta.donaciones.length > 1) {
      setFormOferta({
        ...formOferta,
        donaciones: formOferta.donaciones.filter((_, i) => i !== index),
      });
    }
  };

  const actualizarDonacionOferta = (index, campo, valor) => {
    const nuevasDonaciones = [...formOferta.donaciones];
    nuevasDonaciones[index][campo] = valor;
    setFormOferta({ ...formOferta, donaciones: nuevasDonaciones });
  };

  const enviarOferta = async () => {
    // Validar que el usuario tenga id_organizacion
    if (!user?.id_organizacion) {
      setError("Error: No se pudo obtener la organización del usuario");
      return;
    }

    if (!formOferta.idOferta) {
      setError("Complete el ID de oferta");
      return;
    }

    const donacionesValidas = formOferta.donaciones.filter(
      (d) => d.categoria.trim() && d.descripcion.trim() && d.cantidad.trim()
    );

    if (donacionesValidas.length === 0) {
      setError("Agregue al menos una donación válida con cantidad");
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const oferta = {
        idOferta: parseInt(formOferta.idOferta),
        idOrganizacionDonante: user.id_organizacion, // Usar la org del usuario logueado
        donaciones: donacionesValidas,
      };

      const response = await fetch(
        `${API_BASE_URL}/api/solicitudes/ofertas/crear`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(oferta),
        }
      );

      if (!response.ok) throw new Error("Error al enviar oferta");

      setModalCrearOferta(false);
      setFormOferta({
        idOferta: "",
        donaciones: [{ categoria: "", descripcion: "", cantidad: "" }],
      });
      alert("Oferta enviada correctamente");
    } catch (error) {
      console.error("Error:", error);
      setError("Error al enviar la oferta");
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // FUNCIONES PARA DAR DE BAJA
  // ============================================

  const enviarBaja = async () => {
    // Validar que el usuario tenga id_organizacion
    if (!user?.id_organizacion) {
      setError("Error: No se pudo obtener la organización del usuario");
      return;
    }

    if (!formBaja.idSolicitud) {
      setError("Complete el ID de solicitud");
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const baja = {
        idOrganizacionSolicitante: user.id_organizacion, // Usar la org del usuario logueado
        idSolicitud: parseInt(formBaja.idSolicitud),
      };

      const response = await fetch(`${API_BASE_URL}/api/solicitudes/baja`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(baja),
      });

      if (!response.ok) throw new Error("Error al dar de baja");

      setModalDarBaja(false);
      setFormBaja({ idSolicitud: "" });
      alert("Solicitud dada de baja correctamente");
    } catch (error) {
      console.error("Error:", error);
      setError("Error al dar de baja la solicitud");
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // FUNCIONES PARA TRANSFERIR DONACIONES
  // ============================================

  const abrirModalTransferir = (solicitud) => {
    setSolicitudSeleccionada(solicitud);
    // Pre-llenar el formulario con las donaciones de la solicitud
    setFormTransferencia({
      idOrganizacionDonante: user.id_organizacion || "",
      donaciones: solicitud.donaciones.map((d) => ({
        categoria: d.categoria,
        descripcion: d.descripcion,
        cantidad: "",
      })),
    });
    setModalTransferir(true);
  };

  const agregarDonacionTransferencia = () => {
    setFormTransferencia({
      ...formTransferencia,
      donaciones: [
        ...formTransferencia.donaciones,
        { categoria: "", descripcion: "", cantidad: "" },
      ],
    });
  };

  const eliminarDonacionTransferencia = (index) => {
    if (formTransferencia.donaciones.length > 1) {
      setFormTransferencia({
        ...formTransferencia,
        donaciones: formTransferencia.donaciones.filter((_, i) => i !== index),
      });
    }
  };

  const actualizarDonacionTransferencia = (index, campo, valor) => {
    const nuevasDonaciones = [...formTransferencia.donaciones];
    nuevasDonaciones[index][campo] = valor;
    setFormTransferencia({
      ...formTransferencia,
      donaciones: nuevasDonaciones,
    });
  };

  const enviarTransferencia = async () => {
    if (!user?.id_organizacion) {
      setError("Error: No se pudo obtener la organización del usuario");
      return;
    }

    if (!solicitudSeleccionada) {
      setError("No hay solicitud seleccionada");
      return;
    }

    const donacionesValidas = formTransferencia.donaciones.filter(
      (d) => d.categoria.trim() && d.descripcion.trim() && d.cantidad.trim()
    );

    if (donacionesValidas.length === 0) {
      setError("Agregue al menos una donación válida con cantidad");
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const transferencia = {
        idSolicitud: parseInt(solicitudSeleccionada.idSolicitud),
        idOrganizacionSolicitante: solicitudSeleccionada.idOrganizacion,
        idOrganizacionDonante: user.id_organizacion, // Usar la org del usuario logueado
        donaciones: donacionesValidas,
      };

      console.log("Enviando transferencia:", transferencia);

      const response = await fetch(
        `${API_BASE_URL}/api/solicitudes/transferencias/realizar`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(transferencia),
        }
      );

      if (!response.ok) {
        const errorText = await response.text();
        console.error("Error response:", errorText);
        throw new Error("Error al enviar transferencia");
      }

      setModalTransferir(false);
      setSolicitudSeleccionada(null);
      setFormTransferencia({
        donaciones: [{ categoria: "", descripcion: "", cantidad: "" }],
      });
      alert("Transferencia realizada correctamente");

      // Recargar solicitudes externas
      cargarSolicitudesExternas();
    } catch (error) {
      console.error("Error:", error);
      setError("Error al realizar la transferencia");
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // FUNCIONES HELPER
  // ============================================

  // Ya no necesitamos agrupar porque el backend ya lo hace
  // const solicitudesAgrupadas = solicitudesExternas (ya viene agrupado)

  // ============================================
  // RENDER
  // ============================================

  return (
    <div className="space-y-6">
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
          {error}
        </div>
      )}

      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Gestión de Solicitudes y Donaciones
          </h1>
          <p className="text-gray-600">
            Gestiona solicitudes, ofertas y transferencias de donaciones
          </p>
        </div>
      </div>

      {/* TABS */}
      <div className="flex flex-wrap gap-2">
        <Button
          variant={activeTab === "solicitudes" ? "primary" : "secondary"}
          onClick={() => setActiveTab("solicitudes")}
        >
          Solicitudes Externas
        </Button>
        <Button
          variant={activeTab === "ofertas" ? "primary" : "secondary"}
          onClick={() => setActiveTab("ofertas")}
        >
          Ofertas Externas
        </Button>
        <Button
          variant={activeTab === "transferencias" ? "primary" : "secondary"}
          onClick={() => setActiveTab("transferencias")}
        >
          Transferencias Recibidas
        </Button>
        <Button
          variant={activeTab === "acciones" ? "primary" : "secondary"}
          onClick={() => setActiveTab("acciones")}
        >
          Acciones
        </Button>
      </div>

      {/* CONTENIDO SEGÚN TAB */}
      {activeTab === "solicitudes" && (
        <div className="space-y-4">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
              <h3 className="text-lg font-medium text-gray-900">
                Solicitudes Externas ({solicitudesExternas.length})
              </h3>
              <Button
                variant="secondary"
                size="sm"
                onClick={cargarSolicitudesExternas}
                disabled={loading}
              >
                <RefreshCw
                  size={16}
                  className={`mr-2 ${loading ? "animate-spin" : ""}`}
                />
                Actualizar
              </Button>
            </div>

            {loading ? (
              <div className="text-center py-8 text-gray-500">Cargando...</div>
            ) : solicitudesExternas.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                No hay solicitudes externas
              </div>
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>ID Solicitud</TableHead>
                    <TableHead>Organización</TableHead>
                    <TableHead>Donaciones Solicitadas</TableHead>
                    <TableHead>Acciones</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {solicitudesExternas.map((solicitud) => (
                    <TableRow key={solicitud.idSolicitud}>
                      <TableData>
                        <div className="font-medium">
                          #{solicitud.idSolicitud}
                        </div>
                      </TableData>
                      <TableData>
                        <div className="text-sm">
                          {solicitud.nombreOrganizacion ||
                            `Org. ${solicitud.idOrganizacion}`}
                        </div>
                      </TableData>
                      <TableData>
                        <div className="space-y-1">
                          {solicitud.donaciones.map((donacion, idx) => (
                            <div
                              key={idx}
                              className="text-sm bg-gray-50 p-2 rounded"
                            >
                              <span className="font-medium">
                                {donacion.categoria}:
                              </span>{" "}
                              {donacion.descripcion}
                            </div>
                          ))}
                        </div>
                      </TableData>
                      <TableData>
                        <Button
                          size="sm"
                          onClick={() => abrirModalTransferir(solicitud)}
                        >
                          <ArrowRight size={16} className="mr-1" />
                          Transferir
                        </Button>
                      </TableData>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </div>
        </div>
      )}

      {activeTab === "ofertas" && (
        <div className="space-y-4">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
              <h3 className="text-lg font-medium text-gray-900">
                Ofertas Externas ({ofertasExternas.length})
              </h3>
              <Button
                variant="secondary"
                size="sm"
                onClick={cargarOfertasExternas}
                disabled={loading}
              >
                <RefreshCw
                  size={16}
                  className={`mr-2 ${loading ? "animate-spin" : ""}`}
                />
                Actualizar
              </Button>
            </div>

            {loading ? (
              <div className="text-center py-8 text-gray-500">Cargando...</div>
            ) : ofertasExternas.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                No hay ofertas externas
              </div>
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>ID Oferta</TableHead>
                    <TableHead>Organización Donante</TableHead>
                    <TableHead>Donaciones Ofrecidas</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {ofertasExternas.map((oferta) => (
                    <TableRow key={oferta.idOferta}>
                      <TableData>
                        <div className="font-medium">#{oferta.idOferta}</div>
                      </TableData>
                      <TableData>
                        <div className="text-sm">
                          {oferta.nombreOrganizacion ||
                            `Org. ${oferta.idOrganizacionDonante}`}
                        </div>
                      </TableData>
                      <TableData>
                        <div className="space-y-1">
                          {oferta.donaciones.map((donacion, idx) => (
                            <div
                              key={idx}
                              className="text-sm bg-gray-50 p-2 rounded"
                            >
                              <span className="font-medium">
                                {donacion.categoria}:
                              </span>{" "}
                              {donacion.descripcion} ({donacion.cantidad})
                            </div>
                          ))}
                        </div>
                      </TableData>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            )}
          </div>
        </div>
      )}

      {activeTab === "transferencias" && (
        <div className="space-y-4">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
              <h3 className="text-lg font-medium text-gray-900">
                Transferencias Recibidas ({transferenciasRecibidas.length})
              </h3>
              <Button
                variant="secondary"
                size="sm"
                onClick={cargarTransferenciasRecibidas}
                disabled={loading}
              >
                <RefreshCw
                  size={16}
                  className={`mr-2 ${loading ? "animate-spin" : ""}`}
                />
                Actualizar
              </Button>
            </div>

            {loading ? (
              <div className="text-center py-8 text-gray-500">Cargando...</div>
            ) : transferenciasRecibidas.length === 0 ? (
              <div className="text-center py-8 text-gray-500">
                No hay transferencias recibidas
              </div>
            ) : (
              <div className="p-6 space-y-4">
                {transferenciasRecibidas.map((transferencia, idx) => (
                  <div
                    key={idx}
                    className="bg-gray-50 p-4 rounded border border-gray-200"
                  >
                    <div className="flex justify-between items-start mb-3">
                      <div>
                        <div className="font-medium text-gray-900">
                          Solicitud #{transferencia.idSolicitud}
                        </div>
                        <div className="text-sm text-gray-600">
                          De:{" "}
                          {transferencia.nombreOrganizacionDonante ||
                            `Org. ${transferencia.idOrganizacionDonante}`}
                        </div>
                        <div className="text-xs text-gray-500">
                          {new Date(
                            transferencia.fechaTransferencia
                          ).toLocaleString()}
                        </div>
                      </div>
                    </div>
                    <div className="mt-2">
                      <div className="text-sm font-medium text-gray-700 mb-1">
                        Donaciones:
                      </div>
                      <div className="space-y-1">
                        {transferencia.donaciones.map((donacion, dIdx) => (
                          <div
                            key={dIdx}
                            className="text-sm bg-white p-2 rounded"
                          >
                            <span className="font-medium">
                              {donacion.categoria}:
                            </span>{" "}
                            {donacion.descripcion} ({donacion.cantidad})
                          </div>
                        ))}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}

      {activeTab === "acciones" && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <h3 className="text-lg font-medium text-gray-900 mb-4">
              Solicitar Donaciones
            </h3>
            <p className="text-sm text-gray-600 mb-4">
              Publica una solicitud de donaciones a la red de ONGs
            </p>
            <Button onClick={() => setModalCrearSolicitud(true)}>
              <Plus size={16} className="mr-2" />
              Crear Solicitud
            </Button>
          </div>

          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <h3 className="text-lg font-medium text-gray-900 mb-4">
              Ofrecer Donaciones
            </h3>
            <p className="text-sm text-gray-600 mb-4">
              Publica una oferta de donaciones disponibles
            </p>
            <Button onClick={() => setModalCrearOferta(true)}>
              <Plus size={16} className="mr-2" />
              Crear Oferta
            </Button>
          </div>

          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <h3 className="text-lg font-medium text-gray-900 mb-4">
              Dar de Baja Solicitud
            </h3>
            <p className="text-sm text-gray-600 mb-4">
              Notifica que diste de baja una solicitud propia
            </p>
            <Button variant="danger" onClick={() => setModalDarBaja(true)}>
              <Trash2 size={16} className="mr-2" />
              Dar de Baja
            </Button>
          </div>
        </div>
      )}

      {/* MODAL: CREAR SOLICITUD */}
      <Modal
        isOpen={modalCrearSolicitud}
        onClose={() => setModalCrearSolicitud(false)}
        title="Nueva Solicitud de Donaciones"
        size="lg"
      >
        <div className="space-y-4">
          {/* Mostrar la organización del usuario (solo lectura) */}
          <div className="bg-blue-50 border border-blue-200 rounded-md p-3">
            <p className="text-sm text-blue-800">
              <strong>Tu Organización:</strong>{" "}
              {user?.id_organizacion || "No definida"}
            </p>
          </div>

          <div>
            <Input
              label="ID de Solicitud *"
              type="number"
              value={formSolicitud.idSolicitud}
              onChange={(e) =>
                setFormSolicitud({
                  ...formSolicitud,
                  idSolicitud: e.target.value,
                })
              }
              placeholder="Ejemplo: 101"
            />
          </div>

          <div>
            <div className="flex justify-between items-center mb-3">
              <h4 className="font-medium">Donaciones Solicitadas</h4>
              <Button
                size="sm"
                variant="success"
                onClick={agregarDonacionSolicitud}
              >
                <Plus size={16} className="mr-1" />
                Agregar
              </Button>
            </div>

            {formSolicitud.donaciones.map((donacion, index) => (
              <div
                key={index}
                className="flex gap-3 mb-3 p-3 bg-gray-50 rounded"
              >
                <div className="flex-1">
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Categoría
                  </label>
                  <select
                    value={donacion.categoria}
                    onChange={(e) =>
                      actualizarDonacionSolicitud(
                        index,
                        "categoria",
                        e.target.value
                      )
                    }
                    className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  >
                    <option value="">Seleccionar...</option>
                    <option value="ALIMENTOS">ALIMENTOS</option>
                    <option value="ROPA">ROPA</option>
                    <option value="JUGUETES">JUGUETES</option>
                    <option value="UTILES_ESCOLARES">ÚTILES ESCOLARES</option>
                  </select>
                </div>
                <div className="flex-1">
                  <Input
                    label="Descripción"
                    value={donacion.descripcion}
                    onChange={(e) =>
                      actualizarDonacionSolicitud(
                        index,
                        "descripcion",
                        e.target.value
                      )
                    }
                    placeholder="Ej: Arroz integral 1kg"
                  />
                </div>
                {formSolicitud.donaciones.length > 1 && (
                  <button
                    onClick={() => eliminarDonacionSolicitud(index)}
                    className="self-end p-2 text-red-600 hover:bg-red-50 rounded"
                  >
                    <Trash2 size={18} />
                  </button>
                )}
              </div>
            ))}
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t">
            <Button
              variant="secondary"
              onClick={() => setModalCrearSolicitud(false)}
            >
              Cancelar
            </Button>
            <Button onClick={enviarSolicitud} disabled={loading}>
              <Send size={16} className="mr-2" />
              Enviar Solicitud
            </Button>
          </div>
        </div>
      </Modal>

      {/* MODAL: CREAR OFERTA */}
      <Modal
        isOpen={modalCrearOferta}
        onClose={() => setModalCrearOferta(false)}
        title="Nueva Oferta de Donaciones"
        size="lg"
      >
        <div className="space-y-4">
          {/* Mostrar la organización del usuario (solo lectura) */}
          <div className="bg-green-50 border border-green-200 rounded-md p-3">
            <p className="text-sm text-green-800">
              <strong>Tu Organización:</strong>{" "}
              {user?.id_organizacion || "No definida"}
            </p>
          </div>

          <div>
            <Input
              label="ID de Oferta *"
              type="number"
              value={formOferta.idOferta}
              onChange={(e) =>
                setFormOferta({ ...formOferta, idOferta: e.target.value })
              }
              placeholder="Ejemplo: 201"
            />
          </div>

          <div>
            <div className="flex justify-between items-center mb-3">
              <h4 className="font-medium">Donaciones Ofrecidas</h4>
              <Button
                size="sm"
                variant="success"
                onClick={agregarDonacionOferta}
              >
                <Plus size={16} className="mr-1" />
                Agregar
              </Button>
            </div>

            {formOferta.donaciones.map((donacion, index) => (
              <div
                key={index}
                className="flex gap-3 mb-3 p-3 bg-gray-50 rounded"
              >
                <div className="flex-1">
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Categoría
                  </label>
                  <select
                    value={donacion.categoria}
                    onChange={(e) =>
                      actualizarDonacionOferta(
                        index,
                        "categoria",
                        e.target.value
                      )
                    }
                    className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  >
                    <option value="">Seleccionar...</option>
                    <option value="ALIMENTOS">ALIMENTOS</option>
                    <option value="ROPA">ROPA</option>
                    <option value="JUGUETES">JUGUETES</option>
                    <option value="UTILES_ESCOLARES">ÚTILES ESCOLARES</option>
                  </select>
                </div>
                <div className="flex-1">
                  <Input
                    label="Descripción"
                    value={donacion.descripcion}
                    onChange={(e) =>
                      actualizarDonacionOferta(
                        index,
                        "descripcion",
                        e.target.value
                      )
                    }
                    placeholder="Ej: Remeras de algodón"
                  />
                </div>
                <div className="flex-1">
                  <Input
                    label="Cantidad"
                    value={donacion.cantidad}
                    onChange={(e) =>
                      actualizarDonacionOferta(
                        index,
                        "cantidad",
                        e.target.value
                      )
                    }
                    placeholder="Ej: 10kg o 50 unidades"
                  />
                </div>
                {formOferta.donaciones.length > 1 && (
                  <button
                    onClick={() => eliminarDonacionOferta(index)}
                    className="self-end p-2 text-red-600 hover:bg-red-50 rounded"
                  >
                    <Trash2 size={18} />
                  </button>
                )}
              </div>
            ))}
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t">
            <Button
              variant="secondary"
              onClick={() => setModalCrearOferta(false)}
            >
              Cancelar
            </Button>
            <Button onClick={enviarOferta} disabled={loading}>
              <Send size={16} className="mr-2" />
              Enviar Oferta
            </Button>
          </div>
        </div>
      </Modal>

      {/* MODAL: DAR DE BAJA SOLICITUD */}
      <Modal
        isOpen={modalDarBaja}
        onClose={() => setModalDarBaja(false)}
        title="Dar de Baja Solicitud"
        size="md"
      >
        <div className="space-y-4">
          <p className="text-gray-600">
            Notifica a la red que diste de baja una solicitud propia
          </p>

          {/* Mostrar la organización del usuario (solo lectura) */}
          <div className="bg-red-50 border border-red-200 rounded-md p-3">
            <p className="text-sm text-red-800">
              <strong>Tu Organización:</strong>{" "}
              {user?.id_organizacion || "No definida"}
            </p>
          </div>

          <Input
            label="ID de Solicitud *"
            type="number"
            value={formBaja.idSolicitud}
            onChange={(e) =>
              setFormBaja({ ...formBaja, idSolicitud: e.target.value })
            }
            placeholder="Ejemplo: 101"
          />

          <div className="flex justify-end gap-3 pt-4 border-t">
            <Button variant="secondary" onClick={() => setModalDarBaja(false)}>
              Cancelar
            </Button>
            <Button variant="danger" onClick={enviarBaja} disabled={loading}>
              <Trash2 size={16} className="mr-2" />
              Dar de Baja
            </Button>
          </div>
        </div>
      </Modal>

      {/* MODAL: TRANSFERIR DONACIONES */}
      <Modal
        isOpen={modalTransferir}
        onClose={() => setModalTransferir(false)}
        title={`Transferir Donaciones - Solicitud #${
          solicitudSeleccionada?.idSolicitud || ""
        }`}
        size="lg"
      >
        <div className="space-y-4">
          {solicitudSeleccionada && (
            <div className="bg-blue-50 p-4 rounded-lg mb-4">
              <h4 className="font-medium text-blue-900 mb-2">
                Solicitud Seleccionada
              </h4>
              <p className="text-sm text-blue-800">
                Organización: {solicitudSeleccionada.idOrganizacion}
              </p>
              <div className="mt-2 space-y-1">
                {solicitudSeleccionada.donaciones.map((d, idx) => (
                  <p key={idx} className="text-sm text-blue-700">
                    • {d.categoria}: {d.descripcion}
                  </p>
                ))}
              </div>
            </div>
          )}

          <Input
            label="ID de Organización Donante *"
            type="number"
            value={formTransferencia.idOrganizacionDonante}
            onChange={(e) =>
              setFormTransferencia({
                ...formTransferencia,
                idOrganizacionDonante: e.target.value,
              })
            }
          />

          <div>
            <div className="flex justify-between items-center mb-3">
              <h4 className="font-medium">Donaciones a Transferir</h4>
              <Button
                size="sm"
                variant="success"
                onClick={agregarDonacionTransferencia}
              >
                <Plus size={16} className="mr-1" />
                Agregar
              </Button>
            </div>

            {formTransferencia.donaciones.map((donacion, index) => (
              <div
                key={index}
                className="flex gap-3 mb-3 p-3 bg-gray-50 rounded"
              >
                <div className="flex-1">
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Categoría
                  </label>
                  <select
                    value={donacion.categoria}
                    onChange={(e) =>
                      actualizarDonacionTransferencia(
                        index,
                        "categoria",
                        e.target.value
                      )
                    }
                    className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  >
                    <option value="">Seleccionar...</option>
                    <option value="ALIMENTOS">ALIMENTOS</option>
                    <option value="ROPA">ROPA</option>
                    <option value="JUGUETES">JUGUETES</option>
                    <option value="UTILES_ESCOLARES">ÚTILES ESCOLARES</option>
                  </select>
                </div>
                <div className="flex-1">
                  <Input
                    label="Descripción"
                    value={donacion.descripcion}
                    onChange={(e) =>
                      actualizarDonacionTransferencia(
                        index,
                        "descripcion",
                        e.target.value
                      )
                    }
                    placeholder="Ej: Arroz integral"
                  />
                </div>
                <div className="flex-1">
                  <Input
                    label="Cantidad"
                    value={donacion.cantidad}
                    onChange={(e) =>
                      actualizarDonacionTransferencia(
                        index,
                        "cantidad",
                        e.target.value
                      )
                    }
                    placeholder="2kg"
                  />
                </div>
                {formTransferencia.donaciones.length > 1 && (
                  <button
                    onClick={() => eliminarDonacionTransferencia(index)}
                    className="self-end p-2 text-red-600 hover:bg-red-50 rounded"
                  >
                    <Trash2 size={18} />
                  </button>
                )}
              </div>
            ))}
          </div>

          <div className="flex justify-end gap-3 pt-4 border-t">
            <Button
              variant="secondary"
              onClick={() => setModalTransferir(false)}
            >
              Cancelar
            </Button>
            <Button onClick={enviarTransferencia} disabled={loading}>
              <ArrowRight size={16} className="mr-2" />
              Realizar Transferencia
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default SolicitudesManagement;
