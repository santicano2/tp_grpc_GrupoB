import { useState } from "react";
import { Plus, Edit, Trash2, Users, Calendar } from "lucide-react";

import { useAuth } from "../../contexts/AuthContext";
import { mockEvents, mockUsers } from "../../data/mockData";

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
import EventForm from "./EventForm";

const EventManagement = () => {
  const { user } = useAuth();
  const [events, setEvents] = useState(mockEvents);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingEvent, setEditingEvent] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [showConfirmDelete, setShowConfirmDelete] = useState(null);

  // SOLO PRESIDENTE, COORDINADOR Y VOLUNTARIO PUEDEN VER ESTA PAGINA
  if (
    !user ||
    !["PRESIDENTE", "COORDINADOR", "VOLUNTARIO"].includes(user.role)
  ) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
        No tienes permisos para acceder a esta sección.
      </div>
    );
  }

  const canCreateEdit =
    user.role === "PRESIDENTE" || user.role === "COORDINADOR";
  const canDelete = user.role === "PRESIDENTE" || user.role === "COORDINADOR";

  const handleOpenModal = (event) => {
    if (!canCreateEdit && !event) return; // SOLO COORDINADOR Y PRESIDENTE PUEDEN CREAR EVENTOS
    setEditingEvent(event || null);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingEvent(null);
  };

  const handleSaveEvent = async (eventData) => {
    setIsLoading(true);
    try {
      if (editingEvent) {
        // actualizar evento existente
        setEvents(
          events.map((event) =>
            event.id === editingEvent.id
              ? {
                  ...event,
                  ...eventData,
                  updatedAt: new Date().toISOString(),
                  updatedBy: user.id,
                }
              : event
          )
        );
      } else {
        // crear nuevo evento
        const newEvent = {
          id: Date.now().toString(),
          ...eventData,
          createdAt: new Date().toISOString(),
          createdBy: user.id,
        };
        setEvents([...events, newEvent]);
      }
      handleCloseModal();
    } catch (error) {
      console.error("Error al guardar evento:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteEvent = (eventId) => {
    const event = events.find((e) => e.id === eventId);
    if (event && new Date(event.dateTime) > new Date()) {
      // solo eliminar eventos futuros
      setEvents(events.filter((e) => e.id !== eventId));
    }
    setShowConfirmDelete(null);
  };

  const handleJoinLeaveEvent = (eventId) => {
    setEvents(
      events.map((event) => {
        if (event.id === eventId) {
          const isParticipating = event.participants.includes(user.id);
          return {
            ...event,
            participants: isParticipating
              ? event.participants.filter((id) => id !== user.id)
              : [...event.participants, user.id],
          };
        }
        return event;
      })
    );
  };

  const getUserName = (userId) => {
    const foundUser = mockUsers.find((u) => u.id === userId);
    return foundUser
      ? `${foundUser.name} ${foundUser.lastName}`
      : "Usuario desconocido";
  };

  const getParticipantNames = (participantIds) => {
    return participantIds.map((id) => getUserName(id)).join(", ");
  };

  const isEventPast = (dateTime) => {
    return new Date(dateTime) <= new Date();
  };

  const upcomingEvents = events.filter((e) => !isEventPast(e.dateTime));
  const pastEvents = events.filter((e) => isEventPast(e.dateTime));

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Eventos Solidarios
          </h1>
          <p className="text-gray-600">
            {user.role === "VOLUNTARIO"
              ? "Consulta y participa en eventos solidarios"
              : "Gestiona los eventos solidarios de la organización"}
          </p>
        </div>
        {canCreateEdit && (
          <Button onClick={() => handleOpenModal()}>
            <Plus className="w-4 h-4 mr-2" />
            Nuevo Evento
          </Button>
        )}
      </div>

      {/* ESTADISTICAS */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white rounded-lg shadow-sm p-4 border border-gray-200">
          <div className="flex items-center">
            <Calendar className="w-8 h-8 text-blue-600 mr-3" />
            <div>
              <div className="text-2xl font-bold text-gray-900">
                {upcomingEvents.length}
              </div>
              <div className="text-sm text-gray-600">Próximos eventos</div>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg shadow-sm p-4 border border-gray-200">
          <div className="flex items-center">
            <Calendar className="w-8 h-8 text-gray-600 mr-3" />
            <div>
              <div className="text-2xl font-bold text-gray-900">
                {pastEvents.length}
              </div>
              <div className="text-sm text-gray-600">Eventos realizados</div>
            </div>
          </div>
        </div>
        <div className="bg-white rounded-lg shadow-sm p-4 border border-gray-200">
          <div className="flex items-center">
            <Users className="w-8 h-8 text-green-600 mr-3" />
            <div>
              <div className="text-2xl font-bold text-gray-900">
                {events.filter((e) => e.participants.includes(user.id)).length}
              </div>
              <div className="text-sm text-gray-600">Mi participación</div>
            </div>
          </div>
        </div>
      </div>

      {/* PROXIMOS EVENTOS */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <div className="px-6 py-4 border-b border-gray-200">
          <h3 className="text-lg font-medium text-gray-900">
            Próximos eventos
          </h3>
        </div>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Evento</TableHead>
              <TableHead>Descripción</TableHead>
              <TableHead>Fecha y Hora</TableHead>
              <TableHead>Participantes</TableHead>
              <TableHead>Acciones</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {upcomingEvents.map((event) => (
              <TableRow key={event.id}>
                <TableData>
                  <div className="font-medium">{event.name}</div>
                  <div className="text-xs text-gray-500">
                    Creado por {getUserName(event.createdBy)}
                  </div>
                </TableData>
                <TableData>
                  <div className="text-sm text-gray-600 max-w-xs truncate">
                    {event.description}
                  </div>
                </TableData>
                <TableData>
                  <div className="text-sm">
                    {new Date(event.dateTime).toLocaleDateString("es-ES", {
                      day: "numeric",
                      month: "long",
                      year: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </div>
                </TableData>
                <TableData>
                  <div className="text-sm">
                    <div className="font-medium">
                      {event.participants.length} personas
                    </div>
                    <div className="text-xs text-gray-500 max-w-xs truncate">
                      {getParticipantNames(event.participants)}
                    </div>
                  </div>
                </TableData>
                <TableData>
                  <div className="flex space-x-2">
                    {user.role === "VOLUNTARIO" ? (
                      <Button
                        size="sm"
                        variant={
                          event.participants.includes(user.id)
                            ? "danger"
                            : "success"
                        }
                        onClick={() => handleJoinLeaveEvent(event.id)}
                      >
                        {event.participants.includes(user.id)
                          ? "Abandonar"
                          : "Participar"}
                      </Button>
                    ) : (
                      <>
                        <button
                          onClick={() => handleOpenModal(event)}
                          className="text-blue-600 hover:text-blue-900"
                          title="Editar evento"
                        >
                          <Edit className="w-4 h-4" />
                        </button>
                        {canDelete && (
                          <button
                            onClick={() => setShowConfirmDelete(event.id)}
                            className="text-red-600 hover:text-red-900"
                            title="Eliminar evento"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        )}
                      </>
                    )}
                  </div>
                </TableData>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {/* EVENTOS PASADOS */}
      {pastEvents.length > 0 && (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200">
          <div className="px-6 py-4 border-b border-gray-200">
            <h3 className="text-lg font-medium text-gray-900">
              Eventos realizados
            </h3>
          </div>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Evento</TableHead>
                <TableHead>Descripción</TableHead>
                <TableHead>Fecha y Hora</TableHead>
                <TableHead>Participantes</TableHead>
                <TableHead>Donaciones</TableHead>
                {canCreateEdit && <TableHead>Acciones</TableHead>}
              </TableRow>
            </TableHeader>
            <TableBody>
              {pastEvents.map((event) => (
                <TableRow key={event.id}>
                  <TableData>
                    <div className="font-medium">{event.name}</div>
                    <div className="text-xs text-gray-500">
                      Creado por {getUserName(event.createdBy)}
                    </div>
                  </TableData>
                  <TableData>
                    <div className="text-sm text-gray-600 max-w-xs truncate">
                      {event.description}
                    </div>
                  </TableData>
                  <TableData>
                    <div className="text-sm">
                      {new Date(event.dateTime).toLocaleDateString("es-ES", {
                        day: "numeric",
                        month: "long",
                        year: "numeric",
                        hour: "2-digit",
                        minute: "2-digit",
                      })}
                    </div>
                  </TableData>
                  <TableData>
                    <div className="text-sm">
                      <div className="font-medium">
                        {event.participants.length} personas
                      </div>
                      <div className="text-xs text-gray-500 max-w-xs truncate">
                        {getParticipantNames(event.participants)}
                      </div>
                    </div>
                  </TableData>
                  <TableData>
                    {event.donations && event.donations.length > 0 ? (
                      <div className="text-sm">
                        <div className="font-medium text-green-600">
                          {event.donations.length} donación(es)
                        </div>
                      </div>
                    ) : (
                      <span className="text-xs text-gray-400">
                        Sin donaciones
                      </span>
                    )}
                  </TableData>
                  {canCreateEdit && (
                    <TableData>
                      <button
                        onClick={() => handleOpenModal(event)}
                        className="text-blue-600 hover:text-blue-900"
                        title="Ver/Editar donaciones"
                      >
                        <Edit className="w-4 h-4" />
                      </button>
                    </TableData>
                  )}
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </div>
      )}

      {/* MODAL FORMULARIO */}
      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingEvent ? "Editar Evento" : "Nuevo Evento"}
        size="lg"
      >
        <EventForm
          event={editingEvent}
          onSave={handleSaveEvent}
          onCancel={handleCloseModal}
          isLoading={isLoading}
        />
      </Modal>

      {/* MODAL CONFIRMACION ELIMINACION */}
      <Modal
        isOpen={!!showConfirmDelete}
        onClose={() => setShowConfirmDelete(null)}
        title="Confirmar Eliminación"
        size="sm"
      >
        <div className="space-y-4">
          <p className="text-gray-600">
            ¿Estás seguro que deseas eliminar este evento?
          </p>
          <div className="bg-yellow-50 border border-yellow-200 p-3 rounded-md">
            <p className="text-sm text-yellow-700">
              <strong>Nota:</strong> Solo se pueden eliminar eventos futuros.
            </p>
          </div>
          <div className="flex justify-end space-x-3">
            <Button
              variant="secondary"
              onClick={() => setShowConfirmDelete(null)}
            >
              Cancelar
            </Button>
            <Button
              variant="danger"
              onClick={() =>
                showConfirmDelete && handleDeleteEvent(showConfirmDelete)
              }
            >
              Eliminar
            </Button>
          </div>
        </div>
      </Modal>
    </div>
  );
};

export default EventManagement;
