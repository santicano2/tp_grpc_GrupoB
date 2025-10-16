import { useState } from 'react';
import { gql } from '@apollo/client';
import { useQuery, useMutation } from '@apollo/client/react';
import { useAuth } from '../../contexts/AuthContext';
import { Card, CardHeader, CardTitle, CardContent } from '../ui/Card';
import Button from '../ui/Button';
import Input from '../ui/Input';
import { Filter, Save, Trash2, Edit2, AlertCircle, Calendar } from 'lucide-react';

// GraphQL Queries y Mutations
const EVENT_PARTICIPATION_REPORT = gql`
  query EventParticipationReport($actorUsername: String!, $filter: EventParticipationReportFilter) {
    eventParticipationReport(actorUsername: $actorUsername, filter: $filter) {
      summary {
        year
        month
        totalEvents
        totalParticipations
      }
      details {
        eventId
        eventName
        user
        participationDate
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
  mutation SaveFilter($actorUsername: String!, $input: SavedFilterInput!, $tipo: String!) {
    saveFilter(actorUsername: $actorUsername, input: $input, tipo: $tipo) {
      id
      name
      type
      filtersJson
    }
  }
`;

const UPDATE_FILTER = gql`
  mutation UpdateFilter($actorUsername: String!, $filterId: Int!, $input: SavedFilterInput!) {
    updateFilter(actorUsername: $actorUsername, filterId: $filterId, input: $input) {
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

const MONTH_NAMES = [
  'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
  'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'
];

export default function EventsReportViewer() {
  const { user } = useAuth();
  
  // Estados del formulario de filtros
  const [filters, setFilters] = useState({
    user: user?.username || '',
    dateFrom: '',
    dateTo: ''
  });

  // Estados para gestión de filtros guardados
  const [showSaveFilter, setShowSaveFilter] = useState(false);
  const [filterName, setFilterName] = useState('');
  const [editingFilter, setEditingFilter] = useState(null);
  const [selectedFilterId, setSelectedFilterId] = useState(null);

  // GraphQL queries y mutations
  const { data: reportData, loading: reportLoading, error: reportError, refetch } = useQuery(
    EVENT_PARTICIPATION_REPORT,
    {
      variables: {
        actorUsername: user?.username,
        filter: {
          user: filters.user || null,
          dateFrom: filters.dateFrom || null,
          dateTo: filters.dateTo || null
        }
      },
      skip: !user?.username
    }
  );

  const { data: savedFiltersData, refetch: refetchFilters } = useQuery(
    LIST_SAVED_FILTERS,
    {
      variables: {
        actorUsername: user?.username,
        tipo: 'EVENTOS'
      },
      skip: !user?.username
    }
  );

  const [saveFilterMutation] = useMutation(SAVE_FILTER, {
    onCompleted: () => {
      refetchFilters();
      setShowSaveFilter(false);
      setFilterName('');
      alert('Filtro guardado exitosamente');
    },
    onError: (error) => {
      alert(`Error al guardar filtro: ${error.message}`);
    }
  });

  const [updateFilterMutation] = useMutation(UPDATE_FILTER, {
    onCompleted: () => {
      refetchFilters();
      setEditingFilter(null);
      setFilterName('');
      alert('Filtro actualizado exitosamente');
    },
    onError: (error) => {
      alert(`Error al actualizar filtro: ${error.message}`);
    }
  });

  const [deleteFilterMutation] = useMutation(DELETE_FILTER, {
    onCompleted: () => {
      refetchFilters();
      alert('Filtro eliminado exitosamente');
    },
    onError: (error) => {
      alert(`Error al eliminar filtro: ${error.message}`);
    }
  });

  // Verificar permisos
  if (!user) {
    return (
      <div className="p-6">
        <Card>
          <CardContent className="p-6">
            <div className="flex items-center gap-2 text-red-600">
              <AlertCircle className="h-5 w-5" />
              <p>Debes iniciar sesión para acceder a este informe.</p>
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  const canViewAllUsers = user.role === 'PRESIDENTE' || user.role === 'COORDINADOR';

  const handleFilterChange = (field, value) => {
    setFilters(prev => ({ ...prev, [field]: value }));
  };

  const handleApplyFilter = () => {
    // Validación: el usuario es obligatorio
    if (!filters.user) {
      alert('El campo Usuario es obligatorio');
      return;
    }
    refetch();
  };

  const handleClearFilters = () => {
    setFilters({
      user: user?.username || '',
      dateFrom: '',
      dateTo: ''
    });
    setSelectedFilterId(null);
  };

  const handleSaveFilter = () => {
    if (!filterName.trim()) {
      alert('Por favor ingresa un nombre para el filtro');
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
            filtersJson
          }
        }
      });
    } else {
      saveFilterMutation({
        variables: {
          actorUsername: user.username,
          input: {
            name: filterName,
            filtersJson
          },
          tipo: 'EVENTOS'
        }
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
    if (confirm('¿Estás seguro de que deseas eliminar este filtro?')) {
      deleteFilterMutation({
        variables: {
          actorUsername: user.username,
          filterId
        }
      });
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return '-';
    try {
      return new Date(dateString).toLocaleString('es-AR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch {
      return dateString;
    }
  };

  const formatMonthYear = (year, month) => {
    return `${MONTH_NAMES[month - 1]} ${year}`;
  };

  const summary = reportData?.eventParticipationReport?.summary || [];
  const details = reportData?.eventParticipationReport?.details || [];
  const savedFilters = savedFiltersData?.listSavedFilters || [];

  // Agrupar detalles por mes para mostrar
  const detailsByMonth = {};
  details.forEach(detail => {
    const date = new Date(detail.participationDate);
    const key = `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}`;
    if (!detailsByMonth[key]) {
      detailsByMonth[key] = [];
    }
    detailsByMonth[key].push(detail);
  });

  return (
    <div className="p-6 space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Informe de Participación en Eventos</h1>
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
              {savedFilters.map(filter => (
                <div
                  key={filter.id}
                  className={`flex items-center gap-2 px-3 py-2 rounded-lg border ${
                    selectedFilterId === filter.id
                      ? 'bg-blue-100 border-blue-500'
                      : 'bg-gray-100 border-gray-300'
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
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">
                Usuario <span className="text-red-500">*</span>
              </label>
              <Input
                type="text"
                value={filters.user}
                onChange={(e) => handleFilterChange('user', e.target.value)}
                placeholder="Username"
                disabled={!canViewAllUsers}
              />
              {!canViewAllUsers && (
                <p className="text-xs text-gray-500 mt-1">
                  Solo puedes ver tu propia participación
                </p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Fecha Desde</label>
              <Input
                type="date"
                value={filters.dateFrom}
                onChange={(e) => handleFilterChange('dateFrom', e.target.value)}
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">Fecha Hasta</label>
              <Input
                type="date"
                value={filters.dateTo}
                onChange={(e) => handleFilterChange('dateTo', e.target.value)}
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
            <Button onClick={() => setShowSaveFilter(!showSaveFilter)} variant="secondary">
              <Save className="h-4 w-4 mr-2" />
              Guardar Filtro Actual
            </Button>
          </div>

          {/* Formulario para guardar filtro */}
          {showSaveFilter && (
            <div className="p-4 bg-gray-50 rounded-lg space-y-2">
              <label className="block text-sm font-medium">
                Nombre del filtro {editingFilter && '(editando)'}
              </label>
              <div className="flex gap-2">
                <Input
                  value={filterName}
                  onChange={(e) => setFilterName(e.target.value)}
                  placeholder="Ej: Mis eventos del último mes"
                  className="flex-1"
                />
                <Button onClick={handleSaveFilter}>
                  {editingFilter ? 'Actualizar' : 'Guardar'}
                </Button>
                <Button
                  onClick={() => {
                    setShowSaveFilter(false);
                    setEditingFilter(null);
                    setFilterName('');
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

      {/* Resumen por Mes */}
      {!reportLoading && summary.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <Calendar className="h-5 w-5" />
              Resumen Mensual
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead>
                  <tr className="border-b">
                    <th className="text-left p-2">Mes</th>
                    <th className="text-right p-2">Total Eventos</th>
                    <th className="text-right p-2">Total Participaciones</th>
                  </tr>
                </thead>
                <tbody>
                  {summary.map((item, idx) => (
                    <tr key={idx} className="border-b hover:bg-gray-50">
                      <td className="p-2 font-medium">{formatMonthYear(item.year, item.month)}</td>
                      <td className="p-2 text-right">{item.totalEvents}</td>
                      <td className="p-2 text-right">{item.totalParticipations}</td>
                    </tr>
                  ))}
                </tbody>
                <tfoot>
                  <tr className="bg-gray-100 font-bold">
                    <td className="p-2">TOTAL</td>
                    <td className="p-2 text-right">
                      {summary.reduce((sum, item) => sum + item.totalEvents, 0)}
                    </td>
                    <td className="p-2 text-right">
                      {summary.reduce((sum, item) => sum + item.totalParticipations, 0)}
                    </td>
                  </tr>
                </tfoot>
              </table>
            </div>
          </CardContent>
        </Card>
      )}

      {/* Detalle por Mes */}
      {!reportLoading && details.length > 0 && (
        <div className="space-y-4">
          <h2 className="text-2xl font-bold">Detalle de Participaciones</h2>
          {Object.keys(detailsByMonth).sort().reverse().map(monthKey => {
            const [year, month] = monthKey.split('-');
            const monthDetails = detailsByMonth[monthKey];
            
            return (
              <Card key={monthKey}>
                <CardHeader>
                  <CardTitle>
                    {formatMonthYear(parseInt(year), parseInt(month))} 
                    <span className="text-sm font-normal ml-2">
                      ({monthDetails.length} participaciones)
                    </span>
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <div className="space-y-2">
                    {monthDetails.map((detail, idx) => (
                      <div key={idx} className="p-3 bg-gray-50 rounded-lg">
                        <div className="flex justify-between items-start">
                          <div>
                            <div className="font-medium text-lg">{detail.eventName}</div>
                            <div className="text-sm text-gray-600">
                              Participante: <span className="font-medium">{detail.user}</span>
                            </div>
                          </div>
                          <div className="text-sm text-gray-600">
                            {formatDate(detail.participationDate)}
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            );
          })}
        </div>
      )}

      {/* Sin resultados */}
      {!reportLoading && summary.length === 0 && (
        <Card>
          <CardContent className="p-6">
            <p className="text-center text-gray-600">
              No se encontraron participaciones en eventos con los filtros aplicados
            </p>
          </CardContent>
        </Card>
      )}
    </div>
  );
}
