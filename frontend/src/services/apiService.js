const API_BASE_URL = "http://localhost:8080";

class ApiService {
  constructor() {
    this.baseURL = API_BASE_URL;
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;

    const config = {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        ...options.headers,
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);

      if (!response.ok) {
        throw new Error(`HTTP error status: ${response.status}`);
      }

      const data = await response.json();
      return data;
    } catch (error) {
      console.error("API request failed:", error);
      throw error;
    }
  }

  createFormData(data) {
    const formData = new URLSearchParams();
    Object.keys(data).forEach((key) => {
      formData.append(key, data[key]);
    });
    return formData;
  }

  // ======== USUARIOS ========

  /**
   * Login de usuario
   * @param {string} login - Username
   * @param {string} password - Contraseña
   * @returns {Promise<Object>} Respuesta del login
   */

  async login(login, password) {
    const formData = this.createFormData({ login, password });

    return this.request("/usuarios/login", {
      method: "POST",
      body: formData,
    });
  }

  /**
   * Crear nuevo usuario
   * @param {Object} userData - Datos del usuario
   * @returns {Promise<Object>} Usuario creado
   */

  async createUser(userData) {
    const formData = this.createFormData(userData);

    return this.request("/usuarios/crear", {
      method: "POST",
      body: formData,
    });
  }

  // ======== EVENTOS ========

  /**
   * Listar todos los eventos
   * @returns {Promise<Array>} Lista de eventos
   */

  async getEvents() {
    return this.request("/eventos/listar");
  }

  /**
   * Crear nuevo evento
   * @param {Object} eventData - Datos del evento
   * @returns {Promise<Object>} Evento creado
   */

  async createEvent(eventData) {
    const formData = this.createFormData(eventData);

    return this.request("/eventos/crear", {
      method: "POST",
      body: formData,
    });
  }

  /**
   * Asignar o remover miembro de evento
   * @param {Object} memberData - Datos de la asignación
   * @returns {Promise<Object>} Evento actualizado
   */

  async assignMemberToEvent(memberData) {
    const formData = this.createFormData(memberData);

    return this.request("/eventos/asignar", {
      method: "POST",
      body: formData,
    });
  }

  // ======== DONACIONES ========

  /**
   * Listar todas las donaciones
   * @returns {Promise<Array>} Lista de donaciones
   */

  async getDonations() {
    return this.request("/donaciones/listar");
  }

  /**
   * Crear nueva donación
   * @param {Object} donationData - Datos de la donación
   * @returns {Promise<Object>} Donación creada
   */

  async createDonation(donationData) {
    const formData = this.createFormData(donationData);

    return this.request("/donaciones/crear", {
      method: "POST",
      body: formData,
    });
  }

  // ======== DASHBOARD ========

  /**
   * Obtener estadísticas del dashboard
   * @returns {Promise<Object>} Estadísticas del dashboard
   */

  async getDashboardStats() {
    return this.request("/dashboard/stats");
  }

  /**
   * Obtener lista de usuarios (para contar activos)
   * @returns {Promise<Array>} Lista de usuarios
   */

  async getUsers() {
    return this.request("/usuarios/listar");
  }
}

const apiService = new ApiService();

export default apiService;
