import { useState } from "react";
import { Plus, Edit, UserX, UserCheck } from "lucide-react";

import { useAuth } from "../../contexts/AuthContext";
import { mockUsers } from "../../data/mockData";

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
import UserForm from "./UserForm";

const UserManagement = () => {
  const { user } = useAuth();
  const [users, setUsers] = useState(mockUsers);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  // solo el presidente puede acceder
  if (user?.role !== "PRESIDENTE") {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
        No tienes permisos para acceder a esta sección.
      </div>
    );
  }

  const handleOpenModal = (userToEdit) => {
    setEditingUser(userToEdit);
    setIsModalOpen(true);
  };

  const handleCloseModal = () => {
    setIsModalOpen(false);
    setEditingUser(null);
  };

  const handleSaveUser = async (userData) => {
    setIsLoading(true);
    try {
      if (editingUser) {
        // actualizar usuario existente
        setUsers(
          users.map((u) =>
            u.id === editingUser.id ? { ...u, ...userData } : u
          )
        );
      } else {
        // crear nuevo usuario
        const newUser = {
          id: Date.now().toString(),
          ...userData,
          createdAt: new Date().toISOString(),
          password: Math.random().toString(36).substring(2, 15), // Password generado aleatoriamente
        };
        setUsers([...users, newUser]);
      }
      handleCloseModal();
    } catch (error) {
      console.error("Error al guardar usuario:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleToggleUserStatus = (userId) => {
    setUsers(
      users.map((u) => (u.id === userId ? { ...u, isActive: !u.isActive } : u))
    );
  };

  const getRoleBadgeColor = (role) => {
    const colors = {
      PRESIDENTE: "bg-purple-100 text-purple-800",
      VOCAL: "bg-blue-100 text-blue-800",
      COORDINADOR: "bg-green-100 text-green-800",
      VOLUNTARIO: "bg-gray-100 text-gray-800",
    };
    return colors[role];
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Gestión de Usuarios
          </h1>
          <p className="text-gray-600">Administra los usuarios del sistema</p>
        </div>
        <Button onClick={() => handleOpenModal()}>
          <Plus className="w-4 h-4 mr-2" />
          Nuevo Usuario
        </Button>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-200">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Usuario</TableHead>
              <TableHead>Nombre Completo</TableHead>
              <TableHead>Email</TableHead>
              <TableHead>Teléfono</TableHead>
              <TableHead>Rol</TableHead>
              <TableHead>Estado</TableHead>
              <TableHead>Acciones</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.id}>
                <TableData>
                  <div className="font-medium">{user.username}</div>
                </TableData>
                <TableData>
                  {user.name} {user.lastName}
                </TableData>
                <TableData>{user.email}</TableData>
                <TableData>{user.phone || "-"}</TableData>
                <TableData>
                  <span
                    className={`px-2 py-1 text-xs font-medium rounded-full ${getRoleBadgeColor(
                      user.role
                    )}`}
                  >
                    {user.role}
                  </span>
                </TableData>
                <TableData>
                  <span
                    className={`px-2 py-1 text-xs font-medium rounded-full ${
                      user.isActive
                        ? "bg-green-100 text-green-800"
                        : "bg-red-100 text-red-800"
                    }`}
                  >
                    {user.isActive ? "Activo" : "Inactivo"}
                  </span>
                </TableData>
                <TableData>
                  <div className="flex space-x-2">
                    <button
                      onClick={() => handleOpenModal(user)}
                      className="text-blue-600 hover:text-blue-900"
                      title="Editar usuario"
                    >
                      <Edit className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => handleToggleUserStatus(user.id)}
                      className={`${
                        user.isActive
                          ? "text-red-600 hover:text-red-900"
                          : "text-green-600 hover:text-green-900"
                      }`}
                      title={
                        user.isActive ? "Desactivar usuario" : "Activar usuario"
                      }
                    >
                      {user.isActive ? (
                        <UserX className="w-4 h-4" />
                      ) : (
                        <UserCheck className="w-4 h-4" />
                      )}
                    </button>
                  </div>
                </TableData>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      <Modal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        title={editingUser ? "Editar Usuario" : "Nuevo Usuario"}
        size="lg"
      >
        <UserForm
          user={editingUser}
          onSave={handleSaveUser}
          onCancel={handleCloseModal}
          isLoading={isLoading}
        />
      </Modal>
    </div>
  );
};

export default UserManagement;