const API_BASE_URL = "http://localhost:8080";

class ApiService {
  constructor() {
    this.baseURL = API_BASE_URL;
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;

    // <-- Cambié esta parte para no fijar siempre content-type -->
    const config = {
      headers: {
        ...options.headers, // ahora el método decide el content-type
      },
      ...options,
    };

    try {
      const response = await fetch(url, config);

      if (!response.ok) {
        throw new Error(`HTTP error status: ${response.status}`);
      }

      // si la respuesta no tiene contenido (204), devolver null
      if (
        response.status === 204 ||
        !response.headers.get("content-type")?.includes("application/json")
      ) {
        return null;
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

  /**
   * Obtener lista de usuarios
   * @returns {Promise<Array>} Lista de usuarios
   */

  async getUsers() {
    return this.request("/usuarios/listar");
  }

  /**
   * Modificar usuario existente
   * @param {number} id - ID del usuario
   * @param {Object} userData - Datos del usuario
   * @param {string} actor - Usuario que realiza la acción
   * @returns {Promise<Object>} Usuario modificado
   */

  async updateUser(id, userData, actor) {
    return this.request(
      `/usuarios/modificar/${id}?actor=${encodeURIComponent(actor)}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
      }
    );
  }

  /**
   * Dar de baja a un usuario
   * @param {string} username - Username del usuario
   * @param {string} actor - Usuario que realiza la acción
   * @returns {Promise<void>}
   */

  async deactivateUser(id, actor) {
    return this.request(
      `/usuarios/baja/${encodeURIComponent(
        id
      )}?actor=${encodeURIComponent(actor)}`,
      {
        method: "DELETE",
      }
    );
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

  // ======== INVENTARIO/DONACIONES ========

  /**
   * Listar todos los items del inventario
   * @returns {Promise<Array>} Lista de items del inventario
   */
  async getInventoryItems() {
    return this.request("/donaciones/listar");
  }

  /**
   * Crear nuevo item de inventario
   * @param {Object} itemData - Datos del item
   * @param {string} actor - Usuario que realiza la acción
   * @returns {Promise<Object>} Item creado
   */
  async createInventoryItem(itemData, actor) {
    const formData = this.createFormData({
      actor,
      categoria: itemData.category,
      descripcion: itemData.description,
      cantidad: itemData.quantity,
    });

    return this.request("/donaciones/crear", {
      method: "POST",
      body: formData,
    });
  }

  /**
   * Modificar item de inventario existente
   * @param {number} id - ID del item
   * @param {Object} itemData - Datos del item
   * @param {string} actor - Usuario que realiza la acción
   * @returns {Promise<Object>} Item modificado
   */
  async updateInventoryItem(id, itemData, actor) {
    return this.request(
      `/donaciones/modificar/${id}?actor=${encodeURIComponent(actor)}`,
      {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          id,
          description: itemData.description,
          quantity: itemData.quantity,
        }),
      }
    );
  }

  /**
   * Eliminar item de inventario
   * @param {number} id - ID del item
   * @param {string} actor - Usuario que realiza la acción
   * @returns {Promise<void>}
   */
  async deleteInventoryItem(id, actor) {
    return this.request(
      `/donaciones/eliminar/${id}?actor=${encodeURIComponent(actor)}`,
      {
        method: "DELETE",
      }
    );
  }

  // ======== DASHBOARD ========

  /**
   * Obtener estadísticas del dashboard
   * @returns {Promise<Object>} Estadísticas del dashboard
   */

  async getDashboardStats() {
    return this.request("/dashboard/stats");
  }
}

const apiService = new ApiService();

export default apiService;
