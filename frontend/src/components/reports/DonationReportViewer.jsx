import { useState } from "react";
import { gql } from "@apollo/client";
import { useQuery, useMutation } from "@apollo/client/react";
import { useAuth } from "../../contexts/AuthContext";
import { Card, CardHeader, CardTitle, CardContent } from "../ui/Card";
import Button from "../ui/Button";
import Input from "../ui/Input";
import {
  Filter,
  Save,
  Trash2,
  Edit2,
  AlertCircle,
  FileSpreadsheet,
} from "lucide-react";

// GraphQL Queries y Mutations
const DONATION_REPORT = gql`
  query DonationReport($actorUsername: String!, $filter: DonationReportFilter) {
    donationReport(actorUsername: $actorUsername, filter: $filter) {
      summary {
        category
        deleted
        totalQuantity
      }
      details {
        id
        category
        description
        quantity
        deleted
        createdAt
        createdBy
        updatedAt
        updatedBy
      }
    }
  }
`;

const LIST_SAVED_FILTERS = gql`
  query ListSavedFilters($actorUsername: String!, $tipo: String!) {
    listSavedFilters(actorUsername: $actorUsername, tipo: $tipo) {
      id
      name
      type
      filtersJson
      createdAt
      updatedAt
    }
  }
`;

const SAVE_FILTER = gql`
  mutation SaveFilter(
    $actorUsername: String!
    $input: SavedFilterInput!
    $tipo: String!
  ) {
    saveFilter(actorUsername: $actorUsername, input: $input, tipo: $tipo) {
      id
      name
      type
      filtersJson
    }
  }
`;

const UPDATE_FILTER = gql`
  mutation UpdateFilter(
    $actorUsername: String!
    $filterId: Int!
    $input: SavedFilterInput!
  ) {
    updateFilter(
      actorUsername: $actorUsername
      filterId: $filterId
      input: $input
    ) {
      id
      name
      filtersJson
    }
  }
`;

const DELETE_FILTER = gql`
  mutation DeleteFilter($actorUsername: String!, $filterId: Int!) {
    deleteFilter(actorUsername: $actorUsername, filterId: $filterId)
  }
`;

const CATEGORIES = ["ALIMENTOS", "ROPA", "JUGUETES", "UTILES_ESCOLARES"];

export default function DonationReportViewer() {
  const { user } = useAuth();

  // Estados del formulario de filtros
  const [filters, setFilters] = useState({
    category: "",
    deleted: null,
    dateFrom: "",
    dateTo: "",
  });

  // Estados para gestión de filtros guardados
  const [showSaveFilter, setShowSaveFilter] = useState(false);
  const [filterName, setFilterName] = useState("");
  const [editingFilter, setEditingFilter] = useState(null);
  const [selectedFilterId, setSelectedFilterId] = useState(null);

  // GraphQL queries y mutations
  const {
    data: reportData,
    loading: reportLoading,
    error: reportError,
    refetch,
  } = useQuery(DONATION_REPORT, {
    variables: {
      actorUsername: user?.username,
      filter: {
        category: filters.category || null,
        deleted: filters.deleted,
        dateFrom: filters.dateFrom || null,
        dateTo: filters.dateTo || null,
      },
    },
    skip: !user?.username,
  });

  const { data: savedFiltersData, refetch: refetchFilters } = useQuery(
    LIST_SAVED_FILTERS,
    {
      variables: {
        actorUsername: user?.username,
        tipo: "DONACIONES",
      },
      skip: !user?.username,
    }
  );

  const [saveFilterMutation] = useMutation(SAVE_FILTER, {
    onCompleted: () => {
      refetchFilters();
      setShowSaveFilter(false);
      setFilterName("");
      alert("Filtro guardado exitosamente");
    },
    onError: (error) => {
      alert(`Error al guardar filtro: ${error.message}`);
    },
  });

  const [updateFilterMutation] = useMutation(UPDATE_FILTER, {
    onCompleted: () => {
      refetchFilters();
      setEditingFilter(null);
      setFilterName("");
      alert("Filtro actualizado exitosamente");
    },
    onError: (error) => {
      alert(`Error al actualizar filtro: ${error.message}`);
    },
  });

  const [deleteFilterMutation] = useMutation(DELETE_FILTER, {
    onCompleted: () => {
      refetchFilters();
      alert("Filtro eliminado exitosamente");
    },
    onError: (error) => {
      alert(`Error al eliminar filtro: ${error.message}`);
    },
  });

  // Verificar permisos (solo PRESIDENTE y VOCAL)
  if (!user || (user.role !== "PRESIDENTE" && user.role !== "VOCAL")) {
    return (
      <div className="p-6">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center gap-2 text-red-600">
              <AlertCircle className="h-5 w-5" />
              <p>
                No tienes permisos para acceder a este informe. Solo PRESIDENTE
                y VOCAL pueden ver esta información.
              </p>
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  const handleFilterChange = (field, value) => {
    setFilters((prev) => ({ ...prev, [field]: value }));
  };

  const handleApplyFilter = () => {
    refetch();
  };

  const handleClearFilters = () => {
    setFilters({
      category: "",
      deleted: null,
      dateFrom: "",
      dateTo: "",
    });
    setSelectedFilterId(null);
  };

  const handleSaveFilter = () => {
    if (!filterName.trim()) {
      alert("Por favor ingresa un nombre para el filtro");
      return;
    }

    const filtersJson = JSON.stringify(filters);

    if (editingFilter) {
      updateFilterMutation({
        variables: {
          actorUsername: user.username,
          filterId: editingFilter.id,
          input: {
            name: filterName,
            filtersJson,
          },
        },
      });
    } else {
      saveFilterMutation({
        variables: {
          actorUsername: user.username,
          input: {
            name: filterName,
            filtersJson,
          },
          tipo: "DONACIONES",
        },
      });
    }
  };

  const handleLoadFilter = (filter) => {
    const loadedFilters = JSON.parse(filter.filtersJson);
    setFilters(loadedFilters);
    setSelectedFilterId(filter.id);
    refetch();
  };

  const handleEditFilter = (filter) => {
    setEditingFilter(filter);
    setFilterName(filter.name);
    const loadedFilters = JSON.parse(filter.filtersJson);
    setFilters(loadedFilters);
    setShowSaveFilter(true);
  };

  const handleDeleteFilter = (filterId) => {
    if (confirm("¿Estás seguro de que deseas eliminar este filtro?")) {
      deleteFilterMutation({
        variables: {
          actorUsername: user.username,
          filterId,
        },
      });
    }
  };

  const handleDownloadExcel = async () => {
    try {
      const queryParams = new URLSearchParams();
      if (filters.category) queryParams.append("category", filters.category);
      if (filters.deleted !== null)
        queryParams.append("deleted", filters.deleted);
      if (filters.dateFrom) queryParams.append("date_from", filters.dateFrom);
      if (filters.dateTo) queryParams.append("date_to", filters.dateTo);

      const response = await fetch(
        `http://localhost:8082/reporte/excel?${queryParams.toString()}`
      );

      if (!response.ok) {
        throw new Error("Error al generar el archivo Excel");
      }

      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `informe_donaciones_${
        new Date().toISOString().split("T")[0]
      }.xlsx`;
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
      document.body.removeChild(a);
    } catch (error) {
      alert(`Error al descargar Excel: ${error.message}`);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return "-";
    try {
      return new Date(dateString).toLocaleString("es-AR");
    } catch {
      return dateString;
    }
  };

  const summary = reportData?.donationReport?.summary || [];
  const details = reportData?.donationReport?.details || [];
  const savedFilters = savedFiltersData?.listSavedFilters || [];

  return (
    <div className="p-6 space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Informe de Donaciones</h1>
        <div className="text-sm text-gray-600">
          Usuario: <span className="font-semibold">{user?.username}</span> |
          Rol: <span className="font-semibold">{user?.role}</span>
        </div>
      </div>

      {/* Filtros Guardados */}
      {savedFilters.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Filter className="h-5 w-5" />
              Filtros Guardados
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="flex flex-wrap gap-2">
              {savedFilters.map((filter) => (
                <div
                  key={filter.id}
                  className={`flex items-center gap-2 px-3 py-2 rounded-lg border ${
                    selectedFilterId === filter.id
                      ? "bg-blue-100 border-blue-500"
                      : "bg-gray-100 border-gray-300"
                  }`}
                >
                  <button
                    onClick={() => handleLoadFilter(filter)}
                    className="font-medium hover:text-blue-600"
                  >
                    {filter.name}
                  </button>
                  <button
                    onClick={() => handleEditFilter(filter)}
                    className="text-gray-600 hover:text-blue-600"
                  >
                    <Edit2 className="h-4 w-4" />
                  </button>
                  <button
                    onClick={() => handleDeleteFilter(filter.id)}
                    className="text-gray-600 hover:text-red-600"
                  >
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {/* Filtros */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Filter className="h-5 w-5" />
            Filtros de Búsqueda
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">
                Categoría
              </label>
              <select
                value={filters.category}
                onChange={(e) => handleFilterChange("category", e.target.value)}
                className="w-full px-3 py-2 border rounded-lg"
              >
                <option value="">Todas</option>
                {CATEGORIES.map((cat) => (
                  <option key={cat} value={cat}>
                    {cat}
                  </option>
                ))}
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Estado</label>
              <select
                value={
                  filters.deleted === null ? "" : filters.deleted.toString()
                }
                onChange={(e) =>
                  handleFilterChange(
                    "deleted",
                    e.target.value === "" ? null : e.target.value === "true"
                  )
                }
                className="w-full px-3 py-2 border rounded-lg"
              >
                <option value="">Todos</option>
                <option value="false">Activos</option>
                <option value="true">Eliminados</option>
              </select>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">
                Fecha Desde
              </label>
              <Input
                type="date"
                value={filters.dateFrom}
                onChange={(e) => handleFilterChange("dateFrom", e.target.value)}
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">
                Fecha Hasta
              </label>
              <Input
                type="date"
                value={filters.dateTo}
                onChange={(e) => handleFilterChange("dateTo", e.target.value)}
              />
            </div>
          </div>

          <div className="flex gap-2 flex-wrap">
            <Button onClick={handleApplyFilter}>
              <Filter className="h-4 w-4 mr-2" />
              Aplicar Filtros
            </Button>
            <Button onClick={handleClearFilters} variant="secondary">
              Limpiar Filtros
            </Button>
            <Button
              onClick={() => setShowSaveFilter(!showSaveFilter)}
              variant="secondary"
            >
              <Save className="h-4 w-4 mr-2" />
              Guardar Filtro Actual
            </Button>
            <Button onClick={handleDownloadExcel} variant="secondary">
              <FileSpreadsheet className="h-4 w-4 mr-2" />
              Descargar Excel
            </Button>
          </div>

          {/* Formulario para guardar filtro */}
          {showSaveFilter && (
            <div className="p-4 bg-gray-50 rounded-lg space-y-2">
              <label className="block text-sm font-medium">
                Nombre del filtro {editingFilter && "(editando)"}
              </label>
              <div className="flex gap-2">
                <Input
                  value={filterName}
                  onChange={(e) => setFilterName(e.target.value)}
                  placeholder="Ej: Donaciones activas de alimentos"
                  className="flex-1"
                />
                <Button onClick={handleSaveFilter}>
                  {editingFilter ? "Actualizar" : "Guardar"}
                </Button>
                <Button
                  onClick={() => {
                    setShowSaveFilter(false);
                    setEditingFilter(null);
                    setFilterName("");
                  }}
                  variant="secondary"
                >
                  Cancelar
                </Button>
              </div>
            </div>
          )}
        </CardContent>
      </Card>

      {/* Errores */}
      {reportError && (
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center gap-2 text-red-600">
              <AlertCircle className="h-5 w-5" />
              <p>Error al cargar el informe: {reportError.message}</p>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Loading */}
      {reportLoading && (
        <Card>
          <CardContent className="p-6">
            <p className="text-center text-gray-600">Cargando informe...</p>
          </CardContent>
        </Card>
      )}

      {/* Resumen Agrupado */}
      {!reportLoading && summary.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Resumen por Categoría</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">Categoría</th>
                    <th className="text-left p-2">Estado</th>
                    <th className="text-right p-2">Cantidad Total</th>
                  </tr>
                </thead>
                <tbody>
                  {summary.map((item, idx) => (
                    <tr key={idx} className="border-b hover:bg-gray-50">
                      <td className="p-2 font-medium">{item.category}</td>
                      <td className="p-2">
                        <span
                          className={`px-2 py-1 rounded-full text-xs ${
                            item.deleted
                              ? "bg-red-100 text-red-700"
                              : "bg-green-100 text-green-700"
                          }`}
                        >
                          {item.deleted ? "Eliminado" : "Activo"}
                        </span>
                      </td>
                      <td className="p-2 text-right font-bold">
                        {item.totalQuantity}
                      </td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr className="bg-gray-100 font-bold">
                    <td className="p-2" colSpan="2">
                      TOTAL GENERAL
                    </td>
                    <td className="p-2 text-right">
                      {summary.reduce(
                        (sum, item) => sum + item.totalQuantity,
                        0
                      )}
                    </td>
                  </tr>
                </tfoot>
              </table>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Detalle de Donaciones */}
      {!reportLoading && details.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>
              Detalle de Donaciones ({details.length} registros)
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full text-sm">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">ID</th>
                    <th className="text-left p-2">Categoría</th>
                    <th className="text-left p-2">Descripción</th>
                    <th className="text-right p-2">Cantidad</th>
                    <th className="text-center p-2">Estado</th>
                    <th className="text-left p-2">Fecha Alta</th>
                    <th className="text-left p-2">Usuario Alta</th>
                    <th className="text-left p-2">Fecha Modificación</th>
                    <th className="text-left p-2">Usuario Modificación</th>
                  </tr>
                </thead>
                <tbody>
                  {details.map((item) => (
                    <tr key={item.id} className="border-b hover:bg-gray-50">
                      <td className="p-2">{item.id}</td>
                      <td className="p-2 font-medium">{item.category}</td>
                      <td className="p-2">{item.description}</td>
                      <td className="p-2 text-right">{item.quantity}</td>
                      <td className="p-2 text-center">
                        <span
                          className={`px-2 py-1 rounded-full text-xs ${
                            item.deleted
                              ? "bg-red-100 text-red-700"
                              : "bg-green-100 text-green-700"
                          }`}
                        >
                          {item.deleted ? "Eliminado" : "Activo"}
                        </span>
                      </td>
                      <td className="p-2">{formatDate(item.createdAt)}</td>
                      <td className="p-2">{item.createdBy}</td>
                      <td className="p-2">{formatDate(item.updatedAt)}</td>
                      <td className="p-2">{item.updatedBy || "-"}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Sin resultados */}
      {!reportLoading && summary.length === 0 && (
        <Card>
          <CardContent className="p-6">
            <p className="text-center text-gray-600">
              No se encontraron donaciones con los filtros aplicados
            </p>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
