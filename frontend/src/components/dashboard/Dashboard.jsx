import { Users, Package, Calendar, AlertCircle } from "lucide-react";

import { useAuth } from "../../contexts/AuthContext";
import { mockUsers, mockInventory, mockEvents } from "../../data/mockData";

const Dashboard = () => {
  const { user } = useAuth();

  const stats = {
    totalUsers: mockUsers.filter((u) => u.isActive).length,
    totalInventory: mockInventory
      .filter((i) => !i.deleted)
      .reduce((sum, item) => sum + item.quantity, 0),
    upcomingEvents: mockEvents.filter((e) => new Date(e.dateTime) > new Date())
      .length,
    pastEvents: mockEvents.filter((e) => new Date(e.dateTime) <= new Date())
      .length,
  };

  const lowStockItems = mockInventory.filter(
    (i) => !i.deleted && i.quantity < 10
  );

  const getWelcomeMessage = () => {
    const hour = new Date().getHours();
    let greeting = "Buen día";
    if (hour >= 12 && hour < 18) greeting = "Buenas tardes";
    if (hour >= 18) greeting = "Buenas noches";

    return `${greeting}, ${user?.name}`;
  };

  const getRoleDescription = (role) => {
    const descriptions = {
      PRESIDENTE:
        "Tenes acceso completo al sistema y podes gestionar usuarios.",
      VOCAL: "Podes gestionar el inventario de donaciones.",
      COORDINADOR: "Podes coordinar y gestionar eventos solidarios.",
      VOLUNTARIO: "Podes consultar y participar en eventos solidarios.",
    };
    return descriptions[role];
  };

  return (
    <div className="space-y-6">
      <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
        <h1 className="text-2xl font-bold text-gray-900 mb-2">
          {getWelcomeMessage()}
        </h1>
        <p className="text-gray-600">{getRoleDescription(user?.role || "")}</p>
      </div>

      {/* ESTADISTICAS */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <Users className="w-8 h-8 text-blue-600" />
            </div>
            <div className="ml-4">
              <div className="text-2xl font-bold text-gray-900">
                {stats.totalUsers}
              </div>
              <div className="text-sm text-gray-600">Usuarios Activos</div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <Package className="w-8 h-8 text-green-600" />
            </div>
            <div className="ml-4">
              <div className="text-2xl font-bold text-gray-900">
                {stats.totalInventory}
              </div>
              <div className="text-sm text-gray-600">Items en inventario</div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <Calendar className="w-8 h-8 text-purple-600" />
            </div>
            <div className="ml-4">
              <div className="text-2xl font-bold text-gray-900">
                {stats.upcomingEvents}
              </div>
              <div className="text-sm text-gray-600">Próximos eventos</div>
            </div>
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <Calendar className="w-8 h-8 text-gray-600" />
            </div>
            <div className="ml-4">
              <div className="text-2xl font-bold text-gray-900">
                {stats.pastEvents}
              </div>
              <div className="text-sm text-gray-600">Eventos realizados</div>
            </div>
          </div>
        </div>
      </div>

      {/* ALERTAS Y ACTIVIDAD */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* ALERTA DE BAJO STOCK */}
        {(user?.role === "PRESIDENTE" || user?.role === "VOCAL") &&
          lowStockItems.length > 0 && (
            <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
              <div className="flex items-center mb-4">
                <AlertCircle className="w-5 h-5 text-orange-500 mr-2" />
                <h3 className="text-lg font-semibold text-gray-900">
                  Items con stock bajo
                </h3>
              </div>
              <div className="space-y-2">
                {lowStockItems.map((item) => (
                  <div
                    key={item.id}
                    className="flex justify-between items-center p-2 bg-orange-50 rounded"
                  >
                    <span className="text-sm font-medium">
                      {item.description}
                    </span>
                    <span className="text-sm text-orange-600">
                      {item.quantity} unidades
                    </span>
                  </div>
                ))}
              </div>
            </div>
          )}

        {/* PROXIMOS EVENTOS */}
        <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
          <h3 className="text-lg font-semibold text-gray-900 mb-4">
            Próximos eventos
          </h3>
          <div className="space-y-3">
            {mockEvents
              .filter((e) => new Date(e.dateTime) > new Date())
              .slice(0, 3)
              .map((event) => (
                <div key={event.id} className="border-l-4 border-blue-500 pl-3">
                  <div className="font-medium text-gray-900">{event.name}</div>
                  <div className="text-sm text-gray-600">
                    {new Date(event.dateTime).toLocaleDateString("es-ES", {
                      day: "numeric",
                      month: "long",
                      year: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </div>
                  <div className="text-xs text-gray-500 mt-1">
                    {event.participants.length} participante(s)
                  </div>
                </div>
              ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
