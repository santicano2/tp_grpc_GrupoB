import { useState, useEffect } from 'react';
import { Plus, Send, RefreshCw, UserPlus, Trash2, Calendar } from 'lucide-react';

import { useAuth } from '../../contexts/AuthContext';

import Button from '../ui/Button';
import Modal from '../ui/Modal';
import Input from '../ui/Input';
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableData,
} from '../ui/Table';

const API_BASE_URL = 'http://localhost:8090';

const EventosExternosManagement = () => {
  const { user } = useAuth();
  
  // Estados para datos
  const [eventosExternos, setEventosExternos] = useState([]);
  const [adhesionesRecibidas, setAdhesionesRecibidas] = useState([]);
  
  // Estados para UI
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [activeTab, setActiveTab] = useState('externos');
  
  // Estados para modales
  const [modalPublicarEvento, setModalPublicarEvento] = useState(false);
  const [modalDarBaja, setModalDarBaja] = useState(false);
  const [modalAdherirse, setModalAdherirse] = useState(false);
  
  // Estados para formulario de publicar evento
  const [formPublicar, setFormPublicar] = useState({
    idOrganizacion: '',
    idEvento: '',
    nombre: '',
    descripcion: '',
    fechaHora: ''
  });
  
  // Estados para formulario de baja
  const [formBaja, setFormBaja] = useState({
    idOrganizacion: '',
    idEvento: ''
  });
  
  // Estados para formulario de adhesión
  const [eventoSeleccionado, setEventoSeleccionado] = useState(null);
  const [formAdhesion, setFormAdhesion] = useState({
    idOrganizacion: '',
    idVoluntario: '',
    nombre: '',
    apellido: '',
    telefono: '',
    email: ''
  });

  // Verificar permisos
  if (!user || !['PRESIDENTE', 'VOCAL', 'COORDINADOR', 'VOLUNTARIO'].includes(user.role)) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
        No tenés permisos para acceder a esta sección.
      </div>
    );
  }

  // ============================================
  // FUNCIONES DE CARGA DE DATOS
  // ============================================
  
  const cargarEventosExternos = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/api/eventos-kafka/externos`);
      if (response.ok) {
        const data = await response.json();
        setEventosExternos(data);
      } else {
        throw new Error('Error al cargar eventos externos');
      }
    } catch (error) {
      console.error('Error:', error);
      setError('Error al cargar los eventos externos');
    } finally {
      setLoading(false);
    }
  };

  const cargarAdhesionesRecibidas = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await fetch(`${API_BASE_URL}/api/eventos-kafka/adhesiones`);
      if (response.ok) {
        const data = await response.json();
        setAdhesionesRecibidas(data);
      } else {
        throw new Error('Error al cargar adhesiones');
      }
    } catch (error) {
      console.error('Error:', error);
      setError('Error al cargar las adhesiones recibidas');
    } finally {
      setLoading(false);
    }
  };

  // Cargar datos según tab activo
  useEffect(() => {
    if (activeTab === 'externos') {
      cargarEventosExternos();
    } else if (activeTab === 'adhesiones') {
      cargarAdhesionesRecibidas();
    }
  }, [activeTab]);

  // ============================================
  // FUNCIONES PARA PUBLICAR EVENTO
  // ============================================
  
  const enviarPublicacion = async () => {
    if (!formPublicar.idOrganizacion || !formPublicar.idEvento || !formPublicar.nombre || 
        !formPublicar.descripcion || !formPublicar.fechaHora) {
      setError('Complete todos los campos');
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const evento = {
        idOrganizacion: parseInt(formPublicar.idOrganizacion),
        idEvento: parseInt(formPublicar.idEvento),
        nombre: formPublicar.nombre,
        descripcion: formPublicar.descripcion,
        fechaHora: formPublicar.fechaHora
      };

      const response = await fetch(`${API_BASE_URL}/api/eventos-kafka/publicar`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(evento)
      });

      if (!response.ok) throw new Error('Error al publicar evento');

      setModalPublicarEvento(false);
      setFormPublicar({
        idOrganizacion: '',
        idEvento: '',
        nombre: '',
        descripcion: '',
        fechaHora: ''
      });
      alert('Evento publicado correctamente en la red');
    } catch (error) {
      console.error('Error:', error);
      setError('Error al publicar el evento');
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // FUNCIONES PARA DAR DE BAJA
  // ============================================
  
  const enviarBaja = async () => {
    if (!formBaja.idOrganizacion || !formBaja.idEvento) {
      setError('Complete todos los campos');
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const baja = {
        idOrganizacion: parseInt(formBaja.idOrganizacion),
        idEvento: parseInt(formBaja.idEvento)
      };

      const response = await fetch(`${API_BASE_URL}/api/eventos-kafka/baja`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(baja)
      });

      if (!response.ok) throw new Error('Error al dar de baja');

      setModalDarBaja(false);
      setFormBaja({ idOrganizacion: '', idEvento: '' });
      alert('Evento dado de baja correctamente');
    } catch (error) {
      console.error('Error:', error);
      setError('Error al dar de baja el evento');
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // FUNCIONES PARA ADHERIRSE A EVENTO
  // ============================================
  
  const abrirModalAdherirse = (evento) => {
    setEventoSeleccionado(evento);
    setFormAdhesion({
      idOrganizacion: '',
      idVoluntario: '',
      nombre: user?.name || '',
      apellido: user?.lastName || '',
      telefono: '',
      email: user?.email || ''
    });
    setModalAdherirse(true);
  };

  const enviarAdhesion = async () => {
    if (!formAdhesion.idOrganizacion || !formAdhesion.idVoluntario || 
        !formAdhesion.nombre || !formAdhesion.apellido || !formAdhesion.email) {
      setError('Complete todos los campos obligatorios');
      return;
    }

    if (!eventoSeleccionado) {
      setError('No hay evento seleccionado');
      return;
    }

    setLoading(true);
    setError(null);
    try {
      const adhesion = {
        idEvento: eventoSeleccionado.idEvento,
        idOrganizacion: parseInt(formAdhesion.idOrganizacion),
        idVoluntario: parseInt(formAdhesion.idVoluntario),
        nombre: formAdhesion.nombre,
        apellido: formAdhesion.apellido,
        telefono: formAdhesion.telefono,
        email: formAdhesion.email
      };

      // El idOrganizador es el creador del evento
      const idOrganizador = eventoSeleccionado.idOrganizacion || eventoSeleccionado.createdBy;

      const response = await fetch(`${API_BASE_URL}/api/eventos-kafka/adherir-evento/${idOrganizador}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(adhesion)
      });

      if (!response.ok) throw new Error('Error al enviar adhesión');

      setModalAdherirse(false);
      setEventoSeleccionado(null);
      alert('Adhesión enviada correctamente');
    } catch (error) {
      console.error('Error:', error);
      setError('Error al enviar la adhesión');
    } finally {
      setLoading(false);
    }
  };

  // ============================================
  // FUNCIONES HELPER
  // ============================================
  
  const formatearFecha = (fechaISO) => {
    if (!fechaISO) return '-';
    try {
      const fecha = new Date(fechaISO);
      return fecha.toLocaleDateString('es-ES', {
        day: 'numeric',
        month: 'long',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (e) {
      return fechaISO;
    }
  };

  // Agrupar adhesiones por evento
  const adhesionesAgrupadas = adhesionesRecibidas.reduce((acc, adhesion) => {
    const key = adhesion.idEvento;
    if (!acc[key]) {
      acc[key] = {
        idEvento: adhesion.idEvento,
        adhesiones: []
      };
    }
    acc[key].adhesiones.push(adhesion);
    return acc;
  }, {});

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
            Eventos de la Red de ONGs
          </h1>
          <p className="text-gray-600">
            Gestiona eventos externos y adhesiones de voluntarios
          </p>
        </div>
      </div>

      {/* TABS */}
      <div className="flex flex-wrap gap-2">
        <Button
          variant={activeTab === 'externos' ? 'primary' : 'secondary'}
          onClick={() => setActiveTab('externos')}
        >
          Eventos Externos
        </Button>
        <Button
          variant={activeTab === 'adhesiones' ? 'primary' : 'secondary'}
          onClick={() => setActiveTab('adhesiones')}
        >
          Adhesiones Recibidas
        </Button>
        <Button
          variant={activeTab === 'acciones' ? 'primary' : 'secondary'}
          onClick={() => setActiveTab('acciones')}
        >
          Acciones
        </Button>
      </div>

      {/* CONTENIDO SEGÚN TAB */}
      {activeTab === 'externos' && (
        <div className="space-y-4">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
              <h3 className="text-lg font-medium text-gray-900">
                Eventos Externos ({eventosExternos.length})
              </h3>
              <Button
                variant="secondary"
                size="sm"
                onClick={cargarEventosExternos}
                disabled={loading}
              >
                <RefreshCw size={16} className={`mr-2 ${loading ? 'animate-spin' : ''}`} />
                Actualizar
              </Button>
            </div>

            {loading ? (
              <div className="text-center py-8 text-gray-500">Cargando...</div>
            ) : eventosExternos.length === 0 ? (
              <div className="text-center py-8 text-gray-500">No hay eventos externos disponibles</div>
            ) : (
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Evento</TableHead>
                    <TableHead>Organización</TableHead>
                    <TableHead>Descripción</TableHead>
                    <TableHead>Fecha y Hora</TableHead>
                    <TableHead>Acciones</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {eventosExternos.map((evento, idx) => (
                    <TableRow key={idx}>
                      <TableData>
                        <div className="font-medium">{evento.nombre}</div>
                        <div className="text-xs text-gray-500">ID: {evento.idEvento}</div>
                      </TableData>
                      <TableData>
                        <div className="text-sm">Org. {evento.idOrganizacion}</div>
                      </TableData>
                      <TableData>
                        <div className="text-sm text-gray-600 max-w-xs">
                          {evento.descripcion}
                        </div>
                      </TableData>
                      <TableData>
                        <div className="text-sm">{formatearFecha(evento.fechaHora)}</div>
                      </TableData>
                      <TableData>
                        <Button
                          size="sm"
                          variant="success"
                          onClick={() => abrirModalAdherirse(evento)}
                        >
                          <UserPlus size={16} className="mr-1" />
                          Adherirse
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

      {activeTab === 'adhesiones' && (
        <div className="space-y-4">
          <div className="bg-white rounded-lg shadow-sm border border-gray-200">
            <div className="px-6 py-4 border-b border-gray-200 flex justify-between items-center">
              <h3 className="text-lg font-medium text-gray-900">
                Adhesiones Recibidas ({adhesionesRecibidas.length})
              </h3>
              <Button
                variant="secondary"
                size="sm"
                onClick={cargarAdhesionesRecibidas}
                disabled={loading}
              >
                <RefreshCw size={16} className={`mr-2 ${loading ? 'animate-spin' : ''}`} />
                Actualizar
              </Button>
            </div>

            {loading ? (
              <div className="text-center py-8 text-gray-500">Cargando...</div>
            ) : adhesionesRecibidas.length === 0 ? (
              <div className="text-center py-8 text-gray-500">No hay adhesiones recibidas</div>
            ) : (
              <div className="p-6 space-y-4">
                {Object.values(adhesionesAgrupadas).map((grupo) => (
                  <div key={grupo.idEvento} className="border border-gray-200 rounded-lg p-4">
                    <h4 className="font-semibold text-gray-900 mb-3">
                      Evento ID: {grupo.idEvento} ({grupo.adhesiones.length} adhesiones)
                    </h4>
                    <div className="space-y-2">
                      {grupo.adhesiones.map((adhesion, idx) => (
                        <div key={idx} className="bg-gray-50 p-3 rounded flex justify-between items-center">
                          <div>
                            <p className="font-medium text-gray-900">
                              {adhesion.nombre} {adhesion.apellido}
                            </p>
                            <p className="text-sm text-gray-600">
                              {adhesion.email} {adhesion.telefono && `• ${adhesion.telefono}`}
                            </p>
                            <p className="text-xs text-gray-500">
                              Org: {adhesion.idOrganizacion} • Voluntario ID: {adhesion.idVoluntario}
                            </p>
                          </div>
                        </div>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}

      {activeTab === 'acciones' && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <Calendar className="w-8 h-8 text-blue-600 mb-3" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">Publicar Evento</h3>
            <p className="text-sm text-gray-600 mb-4">
              Publica un evento de tu organización a la red de ONGs
            </p>
            <Button onClick={() => setModalPublicarEvento(true)}>
              <Plus size={16} className="mr-2" />
              Publicar Evento
            </Button>
          </div>

          <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
            <Trash2 className="w-8 h-8 text-red-600 mb-3" />
            <h3 className="text-lg font-medium text-gray-900 mb-2">Dar de Baja Evento</h3>
            <p className="text-sm text-gray-600 mb-4">
              Notifica a la red que diste de baja un evento
            </p>
            <Button variant="danger" onClick={() => setModalDarBaja(true)}>
              <Trash2 size={16} className="mr-2" />
              Dar de Baja
            </Button>
          </div>
        </div>
      )}

      {/* MODAL: PUBLICAR EVENTO */}
      <Modal
        isOpen={modalPublicarEvento}
        onClose={() => setModalPublicarEvento(false)}
        title="Publicar Evento a la Red"
        size="lg"
      >
        <div className="space-y-4">
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="ID de Organización *"
              type="number"
              value={formPublicar.idOrganizacion}
              onChange={(e) => setFormPublicar({ ...formPublicar, idOrganizacion: e.target.value })}
            />
            <Input
              label="ID de Evento *"
              type="number"
              value={formPublicar.idEvento}
              onChange={(e) => setFormPublicar({ ...formPublicar, idEvento: e.target.value })}
            />
          </div>

          <Input
            label="Nombre del Evento *"
            value={formPublicar.nombre}
            onChange={(e) => setFormPublicar({ ...formPublicar, nombre: e.target.value })}
            placeholder="Ej: Jornada de Juegos Solidarios"
          />

          <div className="space-y-1">
            <label className="block text-sm font-medium text-gray-700">
              Descripción *
            </label>
            <textarea
              value={formPublicar.descripcion}
              onChange={(e) => setFormPublicar({ ...formPublicar, descripcion: e.target.value })}
              rows={3}
              className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              placeholder="Descripción del evento..."
            />
          </div>

          <Input
            label="Fecha y Hora *"
            type="datetime-local"
            value={formPublicar.fechaHora}
            onChange={(e) => setFormPublicar({ ...formPublicar, fechaHora: e.target.value })}
          />

          <div className="flex justify-end gap-3 pt-4 border-t">
            <Button variant="secondary" onClick={() => setModalPublicarEvento(false)}>
              Cancelar
            </Button>
            <Button onClick={enviarPublicacion} disabled={loading}>
              <Send size={16} className="mr-2" />
              Publicar
            </Button>
          </div>
        </div>
      </Modal>

      {/* MODAL: DAR DE BAJA EVENTO */}
      <Modal
        isOpen={modalDarBaja}
        onClose={() => setModalDarBaja(false)}
        title="Dar de Baja Evento"
        size="md"
      >
        <div className="space-y-4">
          <p className="text-gray-600">
            Notifica a la red que diste de baja un evento publicado
          </p>
          
          <Input
            label="ID de Organización *"
            type="number"
            value={formBaja.idOrganizacion}
            onChange={(e) => setFormBaja({ ...formBaja, idOrganizacion: e.target.value })}
          />
          
          <Input
            label="ID de Evento *"
            type="number"
            value={formBaja.idEvento}
            onChange={(e) => setFormBaja({ ...formBaja, idEvento: e.target.value })}
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

      {/* MODAL: ADHERIRSE A EVENTO */}
      <Modal
        isOpen={modalAdherirse}
        onClose={() => setModalAdherirse(false)}
        title={`Adherirse a: ${eventoSeleccionado?.nombre || ''}`}
        size="lg"
      >
        <div className="space-y-4">
          {eventoSeleccionado && (
            <div className="bg-blue-50 p-4 rounded-lg mb-4">
              <h4 className="font-medium text-blue-900 mb-2">Evento Seleccionado</h4>
              <p className="text-sm text-blue-800">
                <strong>Organización:</strong> {eventoSeleccionado.idOrganizacion}
              </p>
              <p className="text-sm text-blue-800">
                <strong>Fecha:</strong> {formatearFecha(eventoSeleccionado.fechaHora)}
              </p>
              <p className="text-sm text-blue-700 mt-2">
                {eventoSeleccionado.descripcion}
              </p>
            </div>
          )}

          <div className="grid grid-cols-2 gap-4">
            <Input
              label="ID de tu Organización *"
              type="number"
              value={formAdhesion.idOrganizacion}
              onChange={(e) => setFormAdhesion({ ...formAdhesion, idOrganizacion: e.target.value })}
            />
            <Input
              label="ID de Voluntario *"
              type="number"
              value={formAdhesion.idVoluntario}
              onChange={(e) => setFormAdhesion({ ...formAdhesion, idVoluntario: e.target.value })}
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Nombre *"
              value={formAdhesion.nombre}
              onChange={(e) => setFormAdhesion({ ...formAdhesion, nombre: e.target.value })}
            />
            <Input
              label="Apellido *"
              value={formAdhesion.apellido}
              onChange={(e) => setFormAdhesion({ ...formAdhesion, apellido: e.target.value })}
            />
          </div>

          <Input
            label="Email *"
            type="email"
            value={formAdhesion.email}
            onChange={(e) => setFormAdhesion({ ...formAdhesion, email: e.target.value })}
          />

          <Input
            label="Teléfono"
            value={formAdhesion.telefono}
            onChange={(e) => setFormAdhesion({ ...formAdhesion, telefono: e.target.value })}
            placeholder="Opcional"
          />

          <div className="flex justify-end gap-3 pt-4 border-t">
            <Button variant="secondary" onClick={() => setModalAdherirse(false)}>
              Cancelar
            </Button>
            <Button onClick={enviarAdhesion} disabled={loading}>
              <UserPlus size={16} className="mr-2" />
              Enviar Adhesión
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default EventosExternosManagement;