import { useState } from "react";
import { gql } from "@apollo/client";
import { useQuery } from "@apollo/client/react";
import { useAuth } from "../../contexts/AuthContext";
import Button from "../ui/Button";
import { RefreshCw, Package, Users, Calendar } from "lucide-react";
import {
  Table,
  TableHeader,
  TableBody,
  TableRow,
  TableHead,
  TableData,
} from "../ui/Table";

// GraphQL Query
const RESPUESTAS_OFERTAS_REPORT = gql`
  query RespuestasOfertasReport($actorUsername: String!) {
    respuestasOfertasReport(actorUsername: $actorUsername) {
      ofertas {
        idOferta
        solicitudes {
          idOrganizacionSolicitante
          fechaSolicitud
          procesada
          donaciones {
            categoria
            descripcion
            cantidad
          }
        }
      }
    }
  }
`;

const RespuestasOfertasViewer = () => {
  const { user } = useAuth();
  const [error, setError] = useState(null);

  // Query GraphQL
  const {
    data: reportData,
    loading: reportLoading,
    error: reportError,
    refetch,
  } = useQuery(RESPUESTAS_OFERTAS_REPORT, {
    variables: {
      actorUsername: user?.username || user?.email || "",
    },
    skip: !user,
    onError: (err) => {
      setError(err.message);
    },
  });

  // Verificar permisos
  if (!user || !["PRESIDENTE", "VOCAL"].includes(user.role)) {
    return (
      <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
        ⛔ No tenés permisos para acceder a esta sección. Solo PRESIDENTE y
        VOCAL pueden visualizar las respuestas a ofertas.
      </div>
    );
  }

  const ofertas = reportData?.respuestasOfertasReport?.ofertas || [];
  const totalSolicitudes = ofertas.reduce(
    (sum, oferta) => sum + oferta.solicitudes.length,
    0
  );
  const totalDonaciones = ofertas.reduce(
    (sum, oferta) =>
      sum + oferta.solicitudes.reduce((s, sol) => s + sol.donaciones.length, 0),
    0
  );

  const formatearFecha = (fechaISO) => {
    if (!fechaISO) return "-";
    try {
      const fecha = new Date(fechaISO);
      return fecha.toLocaleDateString("es-ES", {
        day: "numeric",
        month: "long",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      });
    } catch (e) {
      return fechaISO;
    }
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold text-gray-900">
            Respuestas a Ofertas de Donaciones
          </h1>
          <p className="text-gray-600">
            Visualiza las solicitudes recibidas agrupadas por oferta
          </p>
        </div>
        <Button
          variant="secondary"
          size="sm"
          onClick={() => refetch()}
          disabled={reportLoading}
        >
          <RefreshCw
            size={16}
            className={`mr-2 ${reportLoading ? "animate-spin" : ""}`}
          />
          Actualizar
        </Button>
      </div>

      {/* Error */}
      {(error || reportError) && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md">
          ❌ {error || reportError.message}
        </div>
      )}

      {/* Estadísticas */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">Total Ofertas</p>
              <p className="text-2xl font-bold text-gray-900">
                {ofertas.length}
              </p>
            </div>
            <Package className="w-8 h-8 text-blue-600" />
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">
                Solicitudes Recibidas
              </p>
              <p className="text-2xl font-bold text-gray-900">
                {totalSolicitudes}
              </p>
            </div>
            <Users className="w-8 h-8 text-green-600" />
          </div>
        </div>

        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-medium text-gray-600">
                Donaciones Solicitadas
              </p>
              <p className="text-2xl font-bold text-gray-900">
                {totalDonaciones}
              </p>
            </div>
            <Calendar className="w-8 h-8 text-purple-600" />
          </div>
        </div>
      </div>

      {/* Loading */}
      {reportLoading && (
        <div className="text-center py-8 text-gray-500">
          <RefreshCw className="animate-spin inline-block mr-2" size={20} />
          Cargando respuestas a ofertas...
        </div>
      )}

      {/* Contenido Principal */}
      {!reportLoading && ofertas.length === 0 && (
        <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-8 text-center text-gray-500">
          📭 No hay respuestas a ofertas registradas
        </div>
      )}

      {!reportLoading && ofertas.length > 0 && (
        <div className="space-y-6">
          {ofertas.map((oferta) => (
            <div
              key={oferta.idOferta}
              className="bg-white rounded-lg shadow-sm border border-gray-200 overflow-hidden"
            >
              {/* Header de la Oferta */}
              <div className="bg-gradient-to-r from-blue-50 to-blue-100 px-6 py-4 border-b border-blue-200">
                <div className="flex items-center justify-between">
                  <div>
                    <h3 className="text-lg font-semibold text-blue-900">
                      Oferta ID: {oferta.idOferta}
                    </h3>
                    <p className="text-sm text-blue-700">
                      {oferta.solicitudes.length} solicitud(es) recibida(s)
                    </p>
                  </div>
                  <Package className="w-8 h-8 text-blue-600" />
                </div>
              </div>

              {/* Solicitudes de la Oferta */}
              <div className="p-6 space-y-4">
                {oferta.solicitudes.map((solicitud, idx) => (
                  <div
                    key={idx}
                    className="border border-gray-200 rounded-lg p-4 bg-gray-50"
                  >
                    {/* Info de la Solicitud */}
                    <div className="flex items-center justify-between mb-3">
                      <div>
                        <p className="text-sm font-medium text-gray-900">
                          📍 Organización: {solicitud.idOrganizacionSolicitante}
                        </p>
                        <p className="text-xs text-gray-600">
                          📅 Fecha: {formatearFecha(solicitud.fechaSolicitud)}
                        </p>
                      </div>
                      <div>
                        {solicitud.procesada ? (
                          <span className="px-3 py-1 text-xs font-semibold text-green-800 bg-green-100 rounded-full">
                            ✅ Procesada
                          </span>
                        ) : (
                          <span className="px-3 py-1 text-xs font-semibold text-yellow-800 bg-yellow-100 rounded-full">
                            ⏳ Pendiente
                          </span>
                        )}
                      </div>
                    </div>

                    {/* Tabla de Donaciones Solicitadas */}
                    <div className="bg-white rounded border border-gray-200 overflow-hidden">
                      <Table>
                        <TableHeader>
                          <TableRow>
                            <TableHead>Categoría</TableHead>
                            <TableHead>Descripción</TableHead>
                            <TableHead>Cantidad</TableHead>
                          </TableRow>
                        </TableHeader>
                        <TableBody>
                          {solicitud.donaciones.map((donacion, dIdx) => (
                            <TableRow key={dIdx}>
                              <TableData>
                                <span className="px-2 py-1 text-xs font-medium bg-blue-100 text-blue-800 rounded">
                                  {donacion.categoria}
                                </span>
                              </TableData>
                              <TableData>{donacion.descripcion}</TableData>
                              <TableData>
                                <span className="font-semibold text-gray-900">
                                  {donacion.cantidad}
                                </span>
                              </TableData>
                            </TableRow>
                          ))}
                        </TableBody>
                      </Table>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default RespuestasOfertasViewer;
