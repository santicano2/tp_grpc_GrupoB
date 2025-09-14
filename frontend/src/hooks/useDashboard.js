import { useState, useEffect } from "react";

import apiService from "../services/apiService";

export const useDashboard = () => {
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalInventory: 0,
    upcomingEvents: 0,
    pastEvents: 0,
    lowStockItems: [],
    nextEvents: [],
  });

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchDashboardData = async () => {
    try {
      setLoading(true);
      setError(null);

      // hacer fetch de todas las listas usando los endpoints existentes
      const [usersData, eventsData, donationsData] = await Promise.all([
        apiService.getUsers(),
        apiService.getEvents(),
        apiService.getDonations(),
      ]);

      // calcular estadísticas de usuarios
      const totalUsers = Array.isArray(usersData)
        ? usersData.filter((user) => user.active === true).length
        : 0;

      // calcular estadísticas de inventario
      let totalInventory = 0;
      const lowStockItems = [];

      if (Array.isArray(donationsData)) {
        donationsData.forEach((item) => {
          if (!item.deleted) {
            totalInventory += item.quantity || 0;

            // items con stock bajo (menos de 10 unidades)
            if ((item.quantity || 0) < 10) {
              lowStockItems.push({
                id: item.id,
                description: item.description,
                quantity: item.quantity || 0,
                category: item.category,
              });
            }
          }
        });
      }

      // calcular estadísticas de eventos
      let upcomingEvents = 0;
      let pastEvents = 0;
      const nextEvents = [];
      const now = new Date();

      if (Array.isArray(eventsData)) {
        const upcomingEventsList = [];

        eventsData.forEach((event) => {
          try {
            const eventDate = new Date(event.whenIso || event.dateTime);

            if (eventDate > now) {
              upcomingEvents++;
              upcomingEventsList.push({
                id: event.id,
                name: event.name,
                whenIso: event.whenIso || event.dateTime,
                participantCount: event.members ? event.members.length : 0,
              });
            } else {
              pastEvents++;
            }
          } catch {
            // si hay error parseando la fecha, lo consideramos como evento pasado
            pastEvents++;
          }
        });

        // ordenar por fecha y tomar los primeros 3
        upcomingEventsList
          .sort((a, b) => new Date(a.whenIso) - new Date(b.whenIso))
          .slice(0, 3)
          .forEach((event) => nextEvents.push(event));
      }

      setStats({
        totalUsers,
        totalInventory,
        upcomingEvents,
        pastEvents,
        lowStockItems,
        nextEvents,
      });
    } catch (err) {
      console.error("Error al obtener datos del dashboard:", err);
      setError("No se pudieron cargar los datos del dashboard");

      // mantener los valores por defecto en caso de error
      setStats({
        totalUsers: 0,
        totalInventory: 0,
        upcomingEvents: 0,
        pastEvents: 0,
        lowStockItems: [],
        nextEvents: [],
      });
    } finally {
      setLoading(false);
    }
  };

  const refreshStats = () => {
    fetchDashboardData();
  };

  useEffect(() => {
    fetchDashboardData();
  }, []);

  return {
    stats,
    loading,
    error,
    refreshStats,
  };
};
