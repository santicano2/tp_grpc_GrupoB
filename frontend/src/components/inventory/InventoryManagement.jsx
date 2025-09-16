import { useState, useEffect } from "react";
import { Plus, Edit, Trash2 } from "lucide-react";

import { useAuth } from "../../contexts/AuthContext";
import apiService from "../../services/apiService";

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
import InventoryForm from "./InventoryForm";

const InventoryManagement = () => {
  const { user } = useAuth();
  const [inventory, setInventory] = useState([]);
  const [users, setUsers] = useState([]); // Agregar estado para usuarios
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [showConfirmDelete, setShowConfirmDelete] = useState(null);
  const [error, setError] = useState(null);
  const [initialLoading, setInitialLoading] = useState(true);

  // cargar inventario y usuarios al montar el componente
  useEffect(() => {
    loadInventory();
    loadUsers();
  }, []);

  // solo presidente y vocal pueden acceder
  if (!user || !["PRESIDENTE", "VOCAL"].includes(user.role)) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
        No tenés permisos para acceder a esta sección.
      </div>
    );
  }

  const loadInventory = async () => {
    try {
      setInitialLoading(true);
      setError(null);
      const items = await apiService.getInventoryItems();
      // filtrar items no eliminados
      setInventory(items.filter((item) => !item.deleted));
    } catch (error) {
      console.error("Error al cargar inventario:", error);
      setError("Error al cargar el inventario");
    } finally {
      setInitialLoading(false);
    }
  };

  const loadUsers = async () => {
    try {
      const usersList = await apiService.getUsers();
      setUsers(usersList);
    } catch (error) {
      console.error("Error al cargar usuarios:", error);
    }
  };

  const handleOpenModal = (item) => {
    setEditingItem(item || null);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingItem(null);
  };

  const handleSaveItem = async (itemData) => {
    setIsLoading(true);
    setError(null);
    try {
      if (editingItem) {
        // actualizar item existente
        const updatedItem = await apiService.updateInventoryItem(
          editingItem.id,
          itemData,
          user.username
        );
        setInventory(
          inventory.map((item) =>
            item.id === editingItem.id ? updatedItem : item
          )
        );
      } else {
        // crear nuevo item
        const newItem = await apiService.createInventoryItem(
          itemData,
          user.username
        );
        setInventory([...inventory, newItem]);
      }
      handleCloseModal();
    } catch (error) {
      console.error("Error al guardar item:", error);
      setError("Error al guardar el item");
    } finally {
      setIsLoading(false);
    }
  };

  const handleDeleteItem = async (itemId) => {
    try {
      await apiService.deleteInventoryItem(itemId, user.username);
      // remover el item de la lista local
      setInventory(inventory.filter((item) => item.id !== itemId));
      setShowConfirmDelete(null);
    } catch (error) {
      console.error("Error al eliminar item:", error);
      setError("Error al eliminar el item");
    }
  };

  const getCategoryBadgeColor = (category) => {
    const colors = {
      ROPA: "bg-blue-100 text-blue-800",
      ALIMENTOS: "bg-green-100 text-green-800",
      JUGUETES: "bg-purple-100 text-purple-800",
      UTILES_ESCOLARES: "bg-orange-100 text-orange-800",
    };
    return colors[category];
  };

  const getCategoryLabel = (category) => {
    const labels = {
      ROPA: "Ropa",
      ALIMENTOS: "Alimentos",
      JUGUETES: "Juguetes",
      UTILES_ESCOLARES: "Útiles Escolares",
    };
    return labels[category];
  };

  const getUserName = (username) => {
    if (!username) return "Usuario desconocido";

    // buscar el usuario en la lista cargada para obtener nombre completo
    const foundUser = users.find((u) => u.username === username);
    if (foundUser) {
      return `${foundUser.name} ${foundUser.lastname}`;
    }

    // si no se encuentra, devolver el username
    return username;
  };

  const getUpdatedByName = (username) => {
    if (!username) return "-";

    // buscar el usuario en la lista cargada para obtener nombre completo
    const foundUser = users.find((u) => u.username === username);
    if (foundUser) {
      return `${foundUser.name} ${foundUser.lastname}`;
    }

    // si no se encuentra, devolver el username
    return username;
  };

  const totalItems = inventory.reduce((sum, item) => sum + item.quantity, 0);
  const lowStockItems = inventory.filter((item) => item.quantity < 10).length;

  // Mostrar loading inicial
  if (initialLoading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="text-gray-600">Cargando inventario...</div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
          {error}
          <button
            onClick={loadInventory}
            className="ml-2 text-red-800 underline hover:no-underline"
          >
            Reintentar
          </button>
        </div>
      )}

      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Inventario de Donaciones
          </h1>
          <p className="text-gray-600">
            Gestiona el inventario de donaciones de la organización
          </p>
        </div>
        <Button onClick={() => handleOpenModal()}>
          <Plus className="w-4 h-4 mr-2" />
          Nuevo Item
        </Button>
      </div>

      {/* ESTADISTICAS */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white rounded-lg shadow-sm p-4 border border-gray-200">
          <div className="text-2xl font-bold text-gray-900">
            {inventory.length}
          </div>
          <div className="text-sm text-gray-600">Tipos de Items</div>
        </div>
        <div className="bg-white rounded-lg shadow-sm p-4 border border-gray-200">
          <div className="text-2xl font-bold text-gray-900">{totalItems}</div>
          <div className="text-sm text-gray-600">Total Unidades</div>
        </div>
        <div className="bg-white rounded-lg shadow-sm p-4 border border-gray-200">
          <div className="text-2xl font-bold text-orange-600">
            {lowStockItems}
          </div>
          <div className="text-sm text-gray-600">Stock Bajo (&lt;10)</div>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Categoría</TableHead>
              <TableHead>Descripción</TableHead>
              <TableHead>Cantidad</TableHead>
              <TableHead>Creado por</TableHead>
              <TableHead>Fecha Creación</TableHead>
              <TableHead>Última Actualización</TableHead>
              <TableHead>Acciones</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {inventory.map((item) => (
              <TableRow key={item.id}>
                <TableData>
                  <span
                    className={`px-2 py-1 text-xs font-medium rounded-full ${getCategoryBadgeColor(
                      item.category
                    )}`}
                  >
                    {getCategoryLabel(item.category)}
                  </span>
                </TableData>
                <TableData>
                  <div className="font-medium">{item.description}</div>
                </TableData>
                <TableData>
                  <span
                    className={`font-medium ${
                      item.quantity < 10 ? "text-orange-600" : "text-gray-900"
                    }`}
                  >
                    {item.quantity}
                  </span>
                  {item.quantity < 10 && (
                    <span className="text-xs text-orange-500 block">
                      Stock bajo
                    </span>
                  )}
                </TableData>
                <TableData>
                  <div className="text-sm">{getUserName(item.createdBy)}</div>
                </TableData>
                <TableData>
                  <div className="text-sm text-gray-600">
                    {new Date(item.createdAt).toLocaleDateString("es-ES")}
                  </div>
                </TableData>
                <TableData>
                  {item.updatedAt ? (
                    <div className="text-sm text-gray-600">
                      {new Date(item.updatedAt).toLocaleDateString("es-ES")}
                      <div className="text-xs text-gray-500">
                        por {getUpdatedByName(item.updatedBy)}
                      </div>
                    </div>
                  ) : (
                    <span className="text-sm text-gray-400">-</span>
                  )}
                </TableData>
                <TableData>
                  <div className="flex space-x-2">
                    <button
                      onClick={() => handleOpenModal(item)}
                      className="text-blue-600 hover:text-blue-900"
                      title="Editar item"
                    >
                      <Edit className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => setShowConfirmDelete(item.id)}
                      className="text-red-600 hover:text-red-900"
                      title="Eliminar item"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>
                </TableData>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {/* MODAL FORMULARIO */}
      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingItem ? "Editar Item" : "Nuevo Item"}
        size="md"
      >
        <InventoryForm
          item={editingItem}
          onSave={handleSaveItem}
          onCancel={handleCloseModal}
          isLoading={isLoading}
        />
      </Modal>

      {/* MODAL ELIMINACION */}
      <Modal
        isOpen={!!showConfirmDelete}
        onClose={() => setShowConfirmDelete(null)}
        title="Confirmar eliminación"
        size="sm"
      >
        <div className="space-y-4">
          <p className="text-gray-600">
            ¿Estás seguro que deseas eliminar este item del inventario?
          </p>
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
                showConfirmDelete && handleDeleteItem(showConfirmDelete)
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

export default InventoryManagement;