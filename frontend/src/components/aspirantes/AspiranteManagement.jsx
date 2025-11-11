import { useState } from "react";
import { RefreshCw, Eye, CheckCircle, XCircle } from "lucide-react";

import { useAuth } from "../../contexts/AuthContext";
import { useAspiranteManagement } from "../../hooks/useAspiranteManagement";

import Button from "../ui/Button";
import Modal from "../ui/Modal";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableData,
} from "../ui/Table";

const AspiranteManagement = () => {
  const { user } = useAuth();
  const {
    aspirantes,
    loading,
    error,
    aceptarAspirante,
    rechazarAspirante,
    refreshAspirantes,
  } = useAspiranteManagement();

  const [selectedAspirante, setSelectedAspirante] = useState(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isRechazarModalOpen, setIsRechazarModalOpen] = useState(false);
  const [motivoRechazo, setMotivoRechazo] = useState("");
  const [actionLoading, setActionLoading] = useState(false);
  const [actionError, setActionError] = useState(null);

  // Solo el presidente puede acceder
  if (user?.role !== "PRESIDENTE") {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
        No tienes permisos para acceder a esta sección.
      </div>
    );
  }

  const handleViewDetails = (aspirante) => {
    setSelectedAspirante(aspirante);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setSelectedAspirante(null);
  };

  const handleAceptar = async (aspirante) => {
    if (!confirm(`¿Estás seguro de aceptar a ${aspirante.nombre} ${aspirante.apellido}?`)) {
      return;
    }

    setActionLoading(true);
    setActionError(null);

    try {
      await aceptarAspirante(aspirante.id);
      alert(`${aspirante.nombre} ${aspirante.apellido} ha sido aceptado. Ahora puede completar su registro.`);
    } catch (err) {
      setActionError(err.message);
    } finally {
      setActionLoading(false);
    }
  };

  const handleOpenRechazarModal = (aspirante) => {
    setSelectedAspirante(aspirante);
    setMotivoRechazo("");
    setIsRechazarModalOpen(true);
  };

  const handleCloseRechazarModal = () => {
    setIsRechazarModalOpen(false);
    setSelectedAspirante(null);
    setMotivoRechazo("");
    setActionError(null);
  };

  const handleRechazar = async (e) => {
    e.preventDefault();

    if (!motivoRechazo.trim()) {
      setActionError("Debes ingresar un motivo de rechazo");
      return;
    }

    setActionLoading(true);
    setActionError(null);

    try {
      await rechazarAspirante(selectedAspirante.id, motivoRechazo);
      alert(`${selectedAspirante.nombre} ${selectedAspirante.apellido} ha sido rechazado.`);
      handleCloseRechazarModal();
    } catch (err) {
      setActionError(err.message);
    } finally {
      setActionLoading(false);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return "-";
    return new Date(dateString).toLocaleDateString("es-ES", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
        <div className="text-center">
          <div className="mx-auto w-16 h-16 bg-blue-600 rounded-xl flex items-center justify-center mb-4 animate-pulse">
            <span className="text-white font-bold text-xl">EC</span>
          </div>
          <h2 className="text-xl font-semibold text-gray-900 mb-2">
            Cargando Aspirantes
          </h2>
          <p className="text-gray-600">Obteniendo datos...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Gestión de Aspirantes
          </h1>
          <p className="text-gray-600">
            Revisa y gestiona las solicitudes para unirse a la organización
          </p>
        </div>
        <Button variant="outline" onClick={refreshAspirantes} disabled={loading}>
          <RefreshCw
            className={`w-4 h-4 mr-2 ${loading ? "animate-spin" : ""}`}
          />
          Actualizar
        </Button>
      </div>

      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
          {error}
        </div>
      )}

      {actionError && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
          {actionError}
        </div>
      )}

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        {aspirantes.length === 0 ? (
          <div className="p-8 text-center text-gray-500">
            No hay aspirantes pendientes en este momento
          </div>
        ) : (
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Nombre Completo</TableHead>
                <TableHead>Email</TableHead>
                <TableHead>Teléfono</TableHead>
                <TableHead>Edad</TableHead>
                <TableHead>Fecha Solicitud</TableHead>
                <TableHead>Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {aspirantes.map((aspirante) => (
                <TableRow key={aspirante.id}>
                  <TableData>
                    <div className="font-medium">
                      {aspirante.nombre} {aspirante.apellido}
                    </div>
                  </TableData>
                  <TableData>{aspirante.email}</TableData>
                  <TableData>{aspirante.telefono || "-"}</TableData>
                  <TableData>{aspirante.edad || "-"}</TableData>
                  <TableData>{formatDate(aspirante.fechaSolicitud)}</TableData>
                  <TableData>
                    <div className="flex space-x-2">
                      <button
                        onClick={() => handleViewDetails(aspirante)}
                        className="text-blue-600 hover:text-blue-900"
                        title="Ver detalles"
                      >
                        <Eye className="w-4 h-4" />
                      </button>
                      <button
                        onClick={() => handleAceptar(aspirante)}
                        className="text-green-600 hover:text-green-900"
                        title="Aceptar aspirante"
                        disabled={actionLoading}
                      >
                        <CheckCircle className="w-4 h-4" />
                      </button>
                      <button
                        onClick={() => handleOpenRechazarModal(aspirante)}
                        className="text-red-600 hover:text-red-900"
                        title="Rechazar aspirante"
                        disabled={actionLoading}
                      >
                        <XCircle className="w-4 h-4" />
                      </button>
                    </div>
                  </TableData>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        )}
      </div>

      {/* Modal de detalles */}
      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title="Detalles del Aspirante"
        size="lg"
      >
        {selectedAspirante && (
          <div className="space-y-4">
            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="text-sm font-medium text-gray-700">
                  Nombre Completo
                </label>
                <p className="text-gray-900">
                  {selectedAspirante.nombre} {selectedAspirante.apellido}
                </p>
              </div>
              <div>
                <label className="text-sm font-medium text-gray-700">
                  Edad
                </label>
                <p className="text-gray-900">{selectedAspirante.edad || "-"}</p>
              </div>
              <div>
                <label className="text-sm font-medium text-gray-700">
                  Email
                </label>
                <p className="text-gray-900">{selectedAspirante.email}</p>
              </div>
              <div>
                <label className="text-sm font-medium text-gray-700">
                  Teléfono
                </label>
                <p className="text-gray-900">
                  {selectedAspirante.telefono || "-"}
                </p>
              </div>
            </div>

            <div>
              <label className="text-sm font-medium text-gray-700">
                Motivo para unirse
              </label>
              <p className="text-gray-900 mt-1">{selectedAspirante.motivo}</p>
            </div>

            <div>
              <label className="text-sm font-medium text-gray-700">
                Fecha de Solicitud
              </label>
              <p className="text-gray-900">
                {formatDate(selectedAspirante.fechaSolicitud)}
              </p>
            </div>

            <div className="flex justify-end space-x-3 pt-4 border-t">
              <Button
                variant="secondary"
                onClick={handleCloseModal}
              >
                Cerrar
              </Button>
              <Button
                variant="outline"
                onClick={() => {
                  handleCloseModal();
                  handleOpenRechazarModal(selectedAspirante);
                }}
              >
                Rechazar
              </Button>
              <Button
                onClick={() => {
                  handleCloseModal();
                  handleAceptar(selectedAspirante);
                }}
              >
                Aceptar
              </Button>
            </div>
          </div>
        )}
      </Modal>

      {/* Modal de rechazo */}
      <Modal
        isOpen={isRechazarModalOpen}
        onClose={handleCloseRechazarModal}
        title="Rechazar Aspirante"
        size="md"
      >
        {selectedAspirante && (
          <form onSubmit={handleRechazar} className="space-y-4">
            <p className="text-gray-700">
              Estás a punto de rechazar a{" "}
              <strong>
                {selectedAspirante.nombre} {selectedAspirante.apellido}
              </strong>
            </p>

            <div>
              <label
                htmlFor="motivoRechazo"
                className="block text-sm font-medium text-gray-700 mb-2"
              >
                Motivo del rechazo *
              </label>
              <textarea
                id="motivoRechazo"
                value={motivoRechazo}
                onChange={(e) => setMotivoRechazo(e.target.value)}
                rows={4}
                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-transparent"
                placeholder="Explica brevemente el motivo del rechazo..."
                disabled={actionLoading}
              />
            </div>

            {actionError && (
              <div className="bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-md text-sm">
                {actionError}
              </div>
            )}

            <div className="flex justify-end space-x-3 pt-4">
              <Button
                type="button"
                variant="secondary"
                onClick={handleCloseRechazarModal}
                disabled={actionLoading}
              >
                Cancelar
              </Button>
              <Button
                type="submit"
                disabled={actionLoading || !motivoRechazo.trim()}
              >
                {actionLoading ? "Rechazando..." : "Confirmar Rechazo"}
              </Button>
            </div>
          </form>
        )}
      </Modal>
    </div>
  );
};

export default AspiranteManagement;