import { Users, Package, Calendar, AlertCircle, RefreshCw } from "lucide-react";

import { useAuth } from "../../contexts/AuthContext";
import { useDashboard } from "../../hooks/useDashboard";

const Dashboard = () => {
  const { user } = useAuth();
  const { stats, loading, error, refreshStats } = useDashboard();

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

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center">
        <div className="text-center">
          <div className="mx-auto w-16 h-16 bg-blue-600 rounded-xl flex items-center justify-center mb-4 animate-pulse">
            <span className="text-white font-bold text-xl">EC</span>
          </div>
          <h2 className="text-xl font-semibold text-gray-900 mb-2">
            Cargando Dashboard
          </h2>
          <p className="text-gray-600">Obteniendo datos...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
        <div className="flex justify-between items-start">
          <div>
            <h1 className="text-2xl font-bold text-gray-900 mb-2">
              {getWelcomeMessage()}
            </h1>
            <p className="text-gray-600">{getRoleDescription(user?.role || "")}</p>
          </div>
          <button
            onClick={refreshStats}
            className="flex items-center gap-2 px-3 py-2 text-sm bg-blue-50 text-blue-600 rounded-lg hover:bg-blue-100 transition-colors"
            disabled={loading}
          >
            <RefreshCw className={`w-4 h-4 ${loading ? 'animate-spin' : ''}`} />
            Actualizar
          </button>
        </div>
        
        {error && (
          <div className="mt-4 p-3 bg-red-50 border border-red-200 rounded-lg">
            <p className="text-red-700 text-sm">{error}</p>
          </div>
        )}
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
          stats.lowStockItems.length > 0 && (
            <div className="bg-white rounded-lg shadow-sm p-6 border border-gray-200">
              <div className="flex items-center mb-4">
                <AlertCircle className="w-5 h-5 text-orange-500 mr-2" />
                <h3 className="text-lg font-semibold text-gray-900">
                  Items con stock bajo
                </h3>
              </div>
              <div className="space-y-2">
                {stats.lowStockItems.map((item) => (
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
            {stats.nextEvents.length > 0 ? (
              stats.nextEvents.map((event) => (
                <div key={event.id} className="border-l-4 border-blue-500 pl-3">
                  <div className="font-medium text-gray-900">{event.name}</div>
                  <div className="text-sm text-gray-600">
                    {new Date(event.whenIso).toLocaleDateString("es-ES", {
                      day: "numeric",
                      month: "long",
                      year: "numeric",
                      hour: "2-digit",
                      minute: "2-digit",
                    })}
                  </div>
                  <div className="text-xs text-gray-500 mt-1">
                    {event.participantCount} participante(s)
                  </div>
                </div>
              ))
            ) : (
              <p className="text-gray-500 text-sm">No hay eventos próximos</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
