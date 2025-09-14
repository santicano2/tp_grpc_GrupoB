import { useState, useEffect } from "react";

import apiService from "../services/apiService";
import { useAuth } from "../contexts/AuthContext";

export const useUserManagement = () => {
  const { user } = useAuth();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      setError(null);
      const userData = await apiService.getUsers();
      setUsers(Array.isArray(userData) ? userData : []);
    } catch (err) {
      console.error("Error al obtener usuarios:", err);
      setError("No se pudieron cargar los usuarios");
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  const createUser = async (userData) => {
    try {
      setError(null);

      // preparar datos para la API
      const userPayload = {
        actor: user?.username,
        username: userData.username,
        nombre: userData.name,
        apellido: userData.lastname,
        email: userData.email,
        phone: userData.phone || "",
        rol: userData.role,
      };

      const response = await apiService.createUser(userPayload);

      // refrescar la lista de usuarios
      await fetchUsers();

      return response;
    } catch (err) {
      console.error("Error al crear usuario:", err);
      const errorMsg = err.message || "No se pudo crear el usuario";
      setError(errorMsg);
      throw new Error(errorMsg);
    }
  };

  const updateUser = async (userId, userData) => {
    try {
      setError(null);

      // preparar datos para la API
      const userPayload = {
        id: userId,
        username: userData.username,
        name: userData.name,
        lastname: userData.lastname,
        email: userData.email,
        phone: userData.phone || "",
        role: userData.role,
        active: userData.active,
      };

      const response = await apiService.updateUser(
        userId,
        userPayload,
        user?.username
      );

      // refrescar la lista de usuarios
      await fetchUsers();

      return response;
    } catch (err) {
      console.error("Error al actualizar usuario:", err);
      const errorMsg = err.message || "No se pudo actualizar el usuario";
      setError(errorMsg);
      throw new Error(errorMsg);
    }
  };

  const deactivateUser = async (username) => {
    try {
      setError(null);

      await apiService.deactivateUser(username, user?.username);

      // refrescar la lista de usuarios
      await fetchUsers();

      return true;
    } catch (err) {
      console.error("Error al desactivar usuario:", err);
      const errorMsg = err.message || "No se pudo desactivar el usuario";
      setError(errorMsg);
      throw new Error(errorMsg);
    }
  };

  const toggleUserStatus = async (targetUser) => {
    try {
      setError(null);

      if (targetUser.active) {
        // si esta activo, desactivar
        await deactivateUser(targetUser.username);
      } else {
        // si esta inactivo, reactivar (modificar con active: true)
        await updateUser(targetUser.id, {
          ...targetUser,
          active: true,
        });
      }

      return true;
    } catch (err) {
      console.error("Error al cambiar estado del usuario:", err);
      const errorMsg =
        err.message || "No se pudo cambiar el estado del usuario";
      setError(errorMsg);
      throw new Error(errorMsg);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return {
    users,
    loading,
    error,
    createUser,
    updateUser,
    deactivateUser,
    toggleUserStatus,
    refreshUsers: fetchUsers,
  };
};
