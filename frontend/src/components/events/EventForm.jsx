import { useState, useEffect } from "react";

import { useAuth } from "../../contexts/AuthContext";
import { mockUsers, mockInventory } from "../../data/mockData";

import Button from "../ui/Button";
import Input from "../ui/Input";

const EventForm = ({ event, onSave, onCancel, isLoading }) => {
  const { user } = useAuth();
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    dateTime: "",
    participants: [],
    donations: [],
  });

  const [errors, setErrors] = useState({});
  const [availableUsers] = useState(
    mockUsers.filter((u) => u.isActive && u.role !== "PRESIDENTE")
  );
  const [availableInventory] = useState(
    mockInventory.filter((i) => !i.deleted)
  );

  useEffect(() => {
    if (event) {
      setFormData({
        name: event.name,
        description: event.description,
        dateTime: event.dateTime,
        participants: event.participants,
        donations: event.donations || [],
      });
    }
  }, [event]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));

    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: "",
      }));
    }
  };

  const handleParticipantToggle = (userId) => {
    setFormData((prev) => ({
      ...prev,
      participants: prev.participants.includes(userId)
        ? prev.participants.filter((id) => id !== userId)
        : [...prev.participants, userId],
    }));
  };

  const handleDonationChange = (index, field, value) => {
    setFormData((prev) => ({
      ...prev,
      donations: prev.donations.map((donation, i) =>
        i === index ? { ...donation, [field]: value } : donation
      ),
    }));
  };

  const addDonation = () => {
    setFormData((prev) => ({
      ...prev,
      donations: [
        ...prev.donations,
        {
          inventoryItemId: "",
          quantity: 0,
          distributedBy: user.id,
          distributedAt: new Date().toISOString(),
        },
      ],
    }));
  };

  const removeDonation = (index) => {
    setFormData((prev) => ({
      ...prev,
      donations: prev.donations.filter((_, i) => i !== index),
    }));
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.name.trim())
      newErrors.name = "El nombre del evento es obligatorio";
    if (!formData.description.trim())
      newErrors.description = "La descripción es obligatoria";
    if (!formData.dateTime)
      newErrors.dateTime = "La fecha y hora son obligatorias";

    // solo validar fechas futuras para eventos nuevos
    if (
      !event &&
      formData.dateTime &&
      new Date(formData.dateTime) <= new Date()
    ) {
      newErrors.dateTime = "La fecha debe ser futura";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validate()) {
      onSave(formData);
    }
  };

  const isEventPast = event && new Date(event.dateTime) <= new Date();
  const getUserName = (userId) => {
    const foundUser = mockUsers.find((u) => u.id === userId);
    return foundUser
      ? `${foundUser.name} ${foundUser.lastName}`
      : "Usuario desconocido";
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      <div className="grid grid-cols-1 gap-4">
        <Input
          label="Nombre del Evento *"
          name="name"
          value={formData.name}
          onChange={handleChange}
          error={errors.name}
          disabled={isLoading}
        />

        <div className="space-y-1">
          <label className="block text-sm font-medium text-gray-700">
            Descripción *
          </label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows={3}
            className={`
              w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm 
              focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500
              disabled:bg-gray-50 disabled:text-gray-500
              ${
                errors.description
                  ? "border-red-300 focus:ring-red-500 focus:border-red-500"
                  : ""
              }
            `}
            disabled={isLoading}
          />
          {errors.description && (
            <p className="text-sm text-red-600">{errors.description}</p>
          )}
        </div>

        <Input
          label="Fecha y Hora *"
          name="dateTime"
          type="datetime-local"
          value={
            formData.dateTime
              ? new Date(formData.dateTime).toISOString().slice(0, 16)
              : ""
          }
          onChange={handleChange}
          error={errors.dateTime}
          disabled={isLoading}
        />
      </div>

      {/* PARTICIPANTES */}
      <div className="space-y-3">
        <h4 className="text-lg font-medium text-gray-900">Participantes</h4>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-2">
          {availableUsers.map((participant) => (
            <label
              key={participant.id}
              className="flex items-center space-x-3 p-2 rounded border hover:bg-gray-50"
            >
              <input
                type="checkbox"
                checked={formData.participants.includes(participant.id)}
                onChange={() => handleParticipantToggle(participant.id)}
                className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                disabled={isLoading}
              />
              <div className="flex-1">
                <div className="font-medium text-sm">
                  {participant.name} {participant.lastName}
                </div>
                <div className="text-xs text-gray-500">{participant.role}</div>
              </div>
            </label>
          ))}
        </div>
      </div>

      {/* DONACIONES - SOLO EVENTOS PASADOS */}
      {isEventPast && (
        <div className="space-y-3 border-t pt-6">
          <div className="flex justify-between items-center">
            <h4 className="text-lg font-medium text-gray-900">
              Donaciones Distribuidas
            </h4>
            <Button
              type="button"
              size="sm"
              onClick={addDonation}
              disabled={isLoading}
            >
              Agregar Donación
            </Button>
          </div>

          {formData.donations.map((donation, index) => (
            <div key={index} className="border rounded-lg p-4 space-y-3">
              <div className="flex justify-between items-start">
                <h5 className="font-medium">Donación {index + 1}</h5>
                <Button
                  type="button"
                  variant="danger"
                  size="sm"
                  onClick={() => removeDonation(index)}
                  disabled={isLoading}
                >
                  Eliminar
                </Button>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Item del Inventario
                  </label>
                  <select
                    value={donation.inventoryItemId}
                    onChange={(e) =>
                      handleDonationChange(
                        index,
                        "inventoryItemId",
                        e.target.value
                      )
                    }
                    className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    disabled={isLoading}
                  >
                    <option value="">Selecciona un item</option>
                    {availableInventory.map((item) => (
                      <option key={item.id} value={item.id}>
                        {item.description} (Stock: {item.quantity})
                      </option>
                    ))}
                  </select>
                </div>

                <Input
                  label="Cantidad Distribuida"
                  type="number"
                  min="0"
                  value={donation.quantity.toString()}
                  onChange={(e) =>
                    handleDonationChange(
                      index,
                      "quantity",
                      parseInt(e.target.value) || 0
                    )
                  }
                  disabled={isLoading}
                />
              </div>

              <div className="text-sm text-gray-600">
                Distribuido por: {getUserName(donation.distributedBy)}
              </div>
            </div>
          ))}
        </div>
      )}

      <div className="flex justify-end space-x-3 pt-4 border-t">
        <Button
          type="button"
          variant="secondary"
          onClick={onCancel}
          disabled={isLoading}
        >
          Cancelar
        </Button>
        <Button type="submit" disabled={isLoading}>
          {isLoading ? "Guardando..." : event ? "Actualizar" : "Crear Evento"}
        </Button>
      </div>
    </form>
  );
};

export default EventForm;
