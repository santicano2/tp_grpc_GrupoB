import { Users, Package, Calendar, Home, LogOut, Menu, X, Send, Globe, FileText, CalendarCheck, Network } from "lucide-react";

import { useAuth } from "../../contexts/AuthContext";

const Sidebar = ({ isOpen, onToggle, activeSection, onSectionChange }) => {
  const { user, logout } = useAuth();

  const menuItems = [
    {
      id: "dashboard",
      label: "Dashboard",
      icon: Home,
      roles: ["PRESIDENTE", "VOCAL", "COORDINADOR", "VOLUNTARIO"],
    },
    {
      id: "users",
      label: "Gestión de Usuarios",
      icon: Users,
      roles: ["PRESIDENTE"],
    },
    {
      id: "inventory",
      label: "Inventario",
      icon: Package,
      roles: ["PRESIDENTE", "VOCAL"],
    },
    {
      id: "events",
      label: "Eventos",
      icon: Calendar,
      roles: ["PRESIDENTE", "COORDINADOR", "VOLUNTARIO"],
    },
    {
    id: "solicitudes",
    label: "Solicitudes",
    icon: Send,
    roles: ["PRESIDENTE", "VOCAL", "COORDINADOR", "VOLUNTARIO"],
  },
  {
  id: "eventos-externos",
  label: "Eventos Externos",
  icon: Globe,
  roles: ["PRESIDENTE", "VOCAL", "COORDINADOR", "VOLUNTARIO"],
},
{
  id: "donation-report",
  label: "Informe de Donaciones",
  icon: FileText,
  roles: ["PRESIDENTE", "VOCAL"],
},
{
  id: "informe-eventos",
  label: "Informe de Eventos",
  icon: CalendarCheck,
  roles: ["PRESIDENTE", "VOCAL", "COORDINADOR", "VOLUNTARIO"],
},
{
  id: "red-ongs",
  label: "Red de ONGs",
  icon: Network,
  roles: ["PRESIDENTE"],
},
  ];

  const filteredMenuItems = menuItems.filter(
    (item) => user && item.roles.includes(user.role)
  );

  return (
    <>
      {/* Overlay Movil */}
      {isOpen && (
        <div
          className="fixed inset-0 bg-black bg-opacity-50 z-20 lg:hidden"
          onClick={onToggle}
        />
      )}

      {/* Sidebar */}
      <div
        className={`
					fixed inset-y-0 left-0 z-30 w-64 bg-white shadow-lg transform transition-transform duration-200 ease-in-out lg:translate-x-0 lg:static lg:inset-0
					${isOpen ? "translate-x-0" : "-translate-x-full"}
				`}
      >
        <div className="flex items-center justify-between h-16 px-6 border-b border-gray-200">
          <div className="flex items-center">
            <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center">
              <span className="text-white font-bold text-sm">EC</span>
            </div>
            <span className="ml-2 text-lg font-semibold text-gray-900 hidden sm:block">
              Empuje Comunitario
            </span>
          </div>
          <button
            onClick={onToggle}
            className="lg:hidden text-gray-500 hover:text-gray-700"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <nav className="mt-8">
          <div className="px-6 mb-6">
            <div className="flex items-center">
              <div className="w-8 h-8 bg-gray-300 rounded-full flex items-center justify-center">
                <span className="text-gray-700 text-sm font-medium">
                  {user?.name.charAt(0)}
                  {user?.lastName.charAt(0)}
                </span>
              </div>
              <div className="ml-3">
                <p className="text-sm font-medium text-gray-900">
                  {user?.name} {user?.lastName}
                </p>
                <p className="text-xs text-gray-500">{user?.role}</p>
              </div>
            </div>
          </div>

          <ul className="space-y-2 px-3">
            {filteredMenuItems.map((item) => {
              const Icon = item.icon;
              const isActive = activeSection === item.id;

              return (
                <li key={item.id}>
                  <button
                    onClick={() => {
                      onSectionChange(item.id);
                      if (window.innerHeight < 1024) {
                        onToggle();
                      }
                    }}
                    className={`
                      w-full flex items-center px-3 py-2 text-sm font-medium rounded-lg transition-colors cursor-pointer
                      ${
                        isActive
                          ? "bg-blue-100 text-blue-900 border-r-2 border-blue-600"
                          : "text-gray-700 hover:bg-gray-100"
                      }
                    `}
                  >
                    <Icon
                      className={`mr-3 w-5 h-5 ${
                        isActive ? "text-blue-600" : "text-gray-500"
                      }`}
                    />
                    {item.label}
                  </button>
                </li>
              );
            })}
          </ul>
        </nav>

        <div className="absolute bottom-0 w-full p-4 border-t border-gray-200">
          <button
            onClick={logout}
            className="w-full flex items-center px-3 py-2 text-sm font-medium text-gray-700 rounded-lg hover:bg-gray-100 transition-colors cursor-pointer"
          >
            <LogOut className="mr-3 w-5 h-5 text-gray-500" />
            Cerrar Sesión
          </button>
        </div>
      </div>
    </>
  );
};

export default Sidebar;
