/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect } from "react";

import apiService from "../services/apiService";

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error("useAuth debe ser usado dentro de un AuthProvider");
  }
  return context;
};

// manejar localStorage
const saveUserToStorage = (userData) => {
  try {
    localStorage.setItem("user", JSON.stringify(userData));
  } catch (error) {
    console.error("Error guardando usuario en localStorage:", error);
  }
};

const getUserFromStorage = () => {
  try {
    const userData = localStorage.getItem("user");
    return userData ? JSON.parse(userData) : null;
  } catch (error) {
    console.error("Error leyendo usuario de localStorage:", error);
    return null;
  }
};

const removeUserFromStorage = () => {
  try {
    localStorage.removeItem("user");
  } catch (error) {
    console.error("Error eliminando usuario de localStorage:", error);
  }
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  // cargar usuario del localStorage al inicializar
  useEffect(() => {
    const savedUser = getUserFromStorage();
    if (savedUser) {
      setUser(savedUser);
    }
    setIsLoading(false);
  }, []);

  const login = async (username, password) => {
    try {
      const response = await apiService.login(username, password);

      if (response && response.ok === true) {
        // mapear la respuesta de la API al formato esperado
        const userData = {
          id: response.username,
          username: response.username,
          name: response.name,
          lastName: response.lastname,
          email: response.email,
          role: response.role,
          isActive: true,
        };
        setUser(userData);
        saveUserToStorage(userData);
        return true;
      } else {
        console.error("Login failed:", response?.message || "Unknown error");
        return false;
      }
    } catch (error) {
      console.error("Login error:", error);
      return false;
    }
  };

  const logout = () => {
    setUser(null);
    removeUserFromStorage();
  };

  const value = {
    user,
    login,
    logout,
    isAuthenticated: !!user,
    isLoading,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
