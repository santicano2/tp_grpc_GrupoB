import { useState, useEffect } from "react";

import Button from "../ui/Button";
import Input from "../ui/Input";
import Select from "../ui/Select";

const InventoryForm = ({ item, onSave, onCancel, isLoading }) => {
  const [formData, setFormData] = useState({
    category: "ROPA",
    description: "",
    quantity: 0,
  });

  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (item) {
      setFormData({
        category: item.category,
        description: item.description,
        quantity: item.quantity,
      });
    }
  }, [item]);

  const handleChange = (e) => {
    const { name, value, type } = e.target;
    const finalValue = type === "number" ? parseInt(value) || 0 : value;

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

    if (!formData.description.trim())
      newErrors.description = "La descripción es obligatoria";
    if (formData.quantity < 0)
      newErrors.quantity = "La cantidad no puede ser negativa";

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
      <Select
        label="Categoría *"
        name="category"
        value={formData.category}
        onChange={handleChange}
        disabled={isLoading || !!item} // deshabilitar cuando se está editando
      >
        <option value="ROPA">Ropa</option>
        <option value="ALIMENTOS">Alimentos</option>
        <option value="JUGUETES">Juguetes</option>
        <option value="UTILES_ESCOLARES">Útiles Escolares</option>
      </Select>

      {item && (
        <div className="text-sm text-gray-600 bg-gray-50 p-2 rounded">
          <strong>Nota:</strong> No se puede modificar la categoría de un item
          existente.
        </div>
      )}

      <Input
        label="Descripción *"
        name="description"
        value={formData.description}
        onChange={handleChange}
        error={errors.description}
        disabled={isLoading}
        placeholder="Ej: Remeras talle M, Cuadernos rayados, etc."
      />

      <Input
        label="Cantidad *"
        name="quantity"
        type="number"
        min="0"
        value={formData.quantity.toString()}
        onChange={handleChange}
        error={errors.quantity}
        disabled={isLoading}
      />

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
          {isLoading ? "Guardando..." : item ? "Actualizar" : "Crear Item"}
        </Button>
      </div>
    </form>
  );
};

export default InventoryForm;
