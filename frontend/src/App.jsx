import { useState } from "react";
import { Menu } from "lucide-react";

import { AuthProvider, useAuth } from "./contexts/AuthContext";

import Login from "./components/auth/Login";
import Dashboard from "./components/dashboard/Dashboard";
import Sidebar from "./components/layout/Sidebar";

const AppContent = () => {
  const { isAuthenticated } = useAuth();
  const [sidebarOpen, setSidebarOpen] = useState(false);
  const [activeSection, setActiveSection] = useState("dashboard");

  if (!isAuthenticated) {
    return <Login />;
  }

  const toggleSidebar = () => setSidebarOpen(!sidebarOpen);

  const renderContent = () => {
    switch (activeSection) {
      case "users":
        // return <UserManagement />; // TODO: Agregar UserManagement
        return <Dashboard />;
      case "inventory":
        // return <InventoryManagement />; // TODO: Agregar InventoryManagement
        return <Dashboard />;
      case "events":
        // return <EventManagement />; // TODO: Agregar EventManagement
        return <Dashboard />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="flex h-screen bg-gray-50">
      <Sidebar
        isOpen={sidebarOpen}
        onToggle={toggleSidebar}
        activeSection={activeSection}
        onSectionChange={setActiveSection}
      />

      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Barra superior */}
        <header className="bg-white shadow-sm border-b border-gray-200 lg:hidden">
          <div className="flex items-center justify-between h-16 px-4">
            <button
              onClick={toggleSidebar}
              className="text-gray-500 hover:text-gray-700"
            >
              <Menu className="h-6 w-6" />
            </button>
            <div className="flex items-center">
              <div className="w-8 h-8 bg-blue-600 rounded-lg flex items-center justify-center">
                <span className="text-white font-bold text-sm">EC</span>
              </div>
              <span className="ml-2 text-lg font-semibold text-gray-900">
                Empuje Comunitario
              </span>
            </div>
          </div>
        </header>

        {/* Contenido principal */}
        <main className="flex-1 overflow-y-auto p-6">{renderContent()}</main>
      </div>
    </div>
  );
};

const App = () => {
  return (
    <AuthProvider>
      <AppContent />
    </AuthProvider>
  );
};

export default App;
