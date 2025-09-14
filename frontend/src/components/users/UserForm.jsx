import { useState, useEffect } from "react";

import Button from "../ui/Button";
import Input from "../ui/Input";
import Select from "../ui/Select";

const UserForm = ({ user, onSave, onCancel, isLoading }) => {
  const [formData, setFormData] = useState({
    username: "",
    name: "",
    lastName: "",
    phone: "",
    email: "",
    role: "VOLUNTARIO",
    isActive: true,
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (user) {
      setFormData({
        username: user.username,
        name: user.name,
        lastName: user.lastName,
        phone: user.phone || "",
        email: user.email,
        role: user.role,
        isActive: user.isActive,
      });
    }
  }, [user]);

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    const finalValue = type === "checkbox" ? e.checked : value;

    setFormData((prev) => ({
      ...prev,
      [name]: finalValue,
    }));

    // limpiar error del campo al modificarlo
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: undefined,
      }));
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.username.trim())
      newErrors.username = "El nombre de usuario es obligatorio";
    if (!formData.name.trim()) newErrors.name = "El nombre es obligatorio";
    if (!formData.lastName.trim())
      newErrors.lastName = "El apellido es obligatorio";
    if (!formData.email.trim()) newErrors.email = "El email es obligatorio";

    // validar formato de email
    if (formData.email && !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = "El formato del email no es válido";
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

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <Input
          label="Nombre de Usuario *"
          name="username"
          value={formData.username}
          onChange={handleChange}
          error={errors.username}
          disabled={isLoading}
        />

        <Select
          label="Rol *"
          name="role"
          value={formData.role}
          onChange={handleChange}
          disabled={isLoading}
        >
          <option value="VOLUNTARIO">Voluntario</option>
          <option value="COORDINADOR">Coordinador</option>
          <option value="VOCAL">Vocal</option>
          <option value="PRESIDENTE">Presidente</option>
        </Select>

        <Input
          label="Nombre *"
          name="name"
          value={formData.name}
          onChange={handleChange}
          error={errors.name}
          disabled={isLoading}
        />

        <Input
          label="Apellido *"
          name="lastName"
          value={formData.lastName}
          onChange={handleChange}
          error={errors.lastName}
          disabled={isLoading}
        />

        <Input
          label="Email *"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
          error={errors.email}
          disabled={isLoading}
        />

        <Input
          label="Teléfono"
          name="phone"
          value={formData.phone}
          onChange={handleChange}
          disabled={isLoading}
        />
      </div>

      <div className="flex items-center">
        <input
          type="checkbox"
          id="isActive"
          name="isActive"
          checked={formData.isActive}
          onChange={handleChange}
          disabled={isLoading}
          className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
        />
        <label htmlFor="isActive" className="ml-2 block text-sm text-gray-900">
          Usuario activo
        </label>
      </div>

      {!user && (
        <div className="bg-blue-50 border border-blue-200 p-4 rounded-md">
          <p className="text-sm text-blue-700">
            <strong>Nota:</strong> Al crear el usuario, se generará
            automáticamente una contraseña que será enviada por email al
            usuario.
          </p>
        </div>
      )}

      <div className="flex justify-end space-x-3 pt-4">
        <Button
          type="button"
          variant="secondary"
          onClick={onCancel}
          disabled={isLoading}
        >
          Cancelar
        </Button>
        <Button type="submit" disabled={isLoading}>
          {isLoading ? "Guardando..." : user ? "Actualizar" : "Crear Usuario"}
        </Button>
      </div>
    </form>
  );
};

export default UserForm;