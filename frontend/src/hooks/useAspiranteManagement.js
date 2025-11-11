import { useState, useEffect, useCallback } from "react";
import { useAuth } from "../contexts/AuthContext";

const API_BASE_URL = "http://localhost:8080";

export const useAspiranteManagement = () => {
  const { user } = useAuth();
  const [aspirantes, setAspirantes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchAspirantes = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);

      const response = await fetch(`${API_BASE_URL}/aspirantes/pendientes`);
      
      if (!response.ok) {
        throw new Error("Error al cargar aspirantes");
      }

      const data = await response.json();
      setAspirantes(data);
    } catch (err) {
      setError(err.message);
      console.error("Error fetching aspirantes:", err);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    if (user?.role === "PRESIDENTE") {
      fetchAspirantes();
    }
  }, [user, fetchAspirantes]);

  const getAspiranteById = async (id) => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/aspirantes/${id}?actor=${user.username}`
      );

      if (!response.ok) {
        throw new Error("Error al obtener aspirante");
      }

      return await response.json();
    } catch (err) {
      throw new Error(err.message);
    }
  };

  const aceptarAspirante = async (id) => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/aspirantes/${id}/aceptar?actor=${user.username}`,
        {
          method: "PUT",
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Error al aceptar aspirante");
      }

      const data = await response.json();
      
      // Actualizar lista
      await fetchAspirantes();
      
      return data;
    } catch (err) {
      throw new Error(err.message);
    }
  };

  const rechazarAspirante = async (id, motivoRechazo) => {
    try {
      const response = await fetch(
        `${API_BASE_URL}/aspirantes/${id}/rechazar?actor=${user.username}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ motivoRechazo }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Error al rechazar aspirante");
      }

      const data = await response.json();
      
      // Actualizar lista
      await fetchAspirantes();
      
      return data;
    } catch (err) {
      throw new Error(err.message);
    }
  };

  const crearSolicitud = async (formData) => {
    try {
      const response = await fetch(`${API_BASE_URL}/aspirantes/solicitar`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(formData),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Error al enviar solicitud");
      }

      return await response.json();
    } catch (err) {
      throw new Error(err.message);
    }
  };

  return {
    aspirantes,
    loading,
    error,
    fetchAspirantes,
    getAspiranteById,
    aceptarAspirante,
    rechazarAspirante,
    crearSolicitud,
    refreshAspirantes: fetchAspirantes,
  };
};