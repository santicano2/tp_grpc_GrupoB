import { useState } from "react";

import { useAuth } from "../../contexts/AuthContext";

import Button from "../ui/Button";
import Input from "../ui/Input";

const Login = () => {
  const [credentials, setCredentials] = useState({
    username: "",
    password: "",
    id_organizacion: "1", // Default para testing
  });
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      const success = await login(
        credentials.username,
        credentials.password,
        credentials.id_organizacion // Pasar la org seleccionada
      );
      if (!success) {
        setError("Credenciales incorrectas o usuario inactivo");
      }
    } catch {
      setError("Error al iniciar sesión. Intente nuevamente.");
    } finally {
      setIsLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCredentials((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 flex items-center justify-center p-4">
      <div className="max-w-md w-full space-y-8">
        <div className="text-center">
          <div className="mx-auto w-16 h-16 bg-blue-600 rounded-xl flex items-center justify-center mb-4">
            <span className="text-white font-bold text-xl">EC</span>
          </div>
          <h2 className="text-3xl font-bold text-gray-900">
            Empuje Comunitario
          </h2>
          <p className="mt-2 text-sm text-gray-600">
            Ingrese sus credenciales para acceder al sistema
          </p>
        </div>

        <form
          className="mt-8 space-y-6 bg-white p-8 rounded-xl shadow-lg"
          onSubmit={handleSubmit}
        >
          <div className="space-y-4">
            <Input
              label="Usuario"
              name="username"
              type="text"
              required
              value={credentials.username}
              onChange={handleChange}
              placeholder="Ingrese su usuario"
            />

            <Input
              label="Contraseña"
              name="password"
              type="password"
              required
              value={credentials.password}
              onChange={handleChange}
              placeholder="Ingrese su contraseña"
            />

            {/* Selector de Organización para Testing Multi-Org */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Organización (Testing Multi-Org)
              </label>
              <select
                name="id_organizacion"
                value={credentials.id_organizacion}
                onChange={handleChange}
                className="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              >
                <option value="1">Organización 1 - Empuje Comunitario</option>
                <option value="2">Organización 2 - Ayuda Vecinal</option>
                <option value="3">Organización 3 - Manos Solidarias</option>
                <option value="4">Organización 4 - Futuro Mejor</option>
              </select>
              <p className="mt-1 text-xs text-gray-500">
                Selecciona la organización que representas para testing
              </p>
            </div>
          </div>

          {error && (
            <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md text-sm">
              {error}
            </div>
          )}

          <Button type="submit" className="w-full" disabled={isLoading}>
            {isLoading ? "Iniciando sesión..." : "Iniciar sesión"}
          </Button>

          {/* Usuarios de prueba */}
          <div className="mt-6 p-4 bg-gray-50 rounded-lg">
            <p className="text-xs text-gray-600 mb-2">Usuarios de prueba:</p>
            <div className="text-xs text-gray-500 space-y-1">
              <div>
                <strong>Presidente:</strong> presidente1 / password123
              </div>
              <div>
                <strong>Vocal:</strong> vocal1 / password123
              </div>
              <div>
                <strong>Coordinador:</strong> coordinador1 / password123
              </div>
              <div>
                <strong>Voluntario:</strong> voluntario1 / password123
              </div>
            </div>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Login;
