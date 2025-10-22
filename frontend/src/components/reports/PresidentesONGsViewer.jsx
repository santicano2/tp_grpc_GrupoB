import { useState } from "react";
import { useAuth } from "../../contexts/AuthContext";
import { Card, CardHeader, CardTitle, CardContent } from "../ui/Card";
import Button from "../ui/Button";
import { Network, Users, Building2, Loader2, AlertCircle } from "lucide-react";

export default function PresidentesONGsViewer() {
  const { user } = useAuth();
  const [orgIds, setOrgIds] = useState("");
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState(null);
  const [error, setError] = useState(null);

  // validar que solo PRESIDENTE pueda acceder
  if (!user || user.role !== "PRESIDENTE") {
    return (
      <div className="flex items-center justify-center h-full">
        <Card className="w-96">
          <CardContent className="pt-6">
            <div className="flex flex-col items-center text-center space-y-4">
              <AlertCircle className="h-12 w-12 text-red-500" />
              <div>
                <h3 className="font-semibold text-lg">Acceso Denegado</h3>
                <p className="text-sm text-gray-600 mt-2">
                  Solo usuarios con rol PRESIDENTE pueden acceder a esta
                  funcionalidad.
                </p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  const handleConsultar = async () => {
    setError(null);

    // validar que se ingresaron IDs
    if (!orgIds.trim()) {
      setError("Por favor ingrese al menos un ID de organización");
      return;
    }

    // parsear IDs (separados por comas)
    const idsArray = orgIds
      .split(",")
      .map((id) => id.trim())
      .filter((id) => id !== "");

    if (idsArray.length === 0) {
      setError("Por favor ingrese IDs válidos");
      return;
    }

    setLoading(true);

    try {
      const response = await fetch(
        "http://localhost:8080/api/consulta-red/consultar",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({ orgIds: idsArray }),
        }
      );

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.error || "Error al consultar la red de ONGs");
      }

      const result = await response.json();
      setData(result);
    } catch (err) {
      console.error("Error:", err);
      setError(err.message || "Error al conectar con el servicio");
    } finally {
      setLoading(false);
    }
  };

  const handleLimpiar = () => {
    setOrgIds("");
    setData(null);
    setError(null);
  };

  return (
    <div className="container mx-auto p-6 space-y-6">
      {/* Header */}
      <div className="flex items-center space-x-3">
        <Network className="h-8 w-8 text-blue-600" />
        <div>
          <h1 className="text-3xl font-bold">Consulta Red de ONGs</h1>
          <p className="text-gray-600">
            Consulte información de presidentes y organizaciones de la red
          </p>
        </div>
      </div>

      {/* Formulario de consulta */}
      <Card>
        <CardHeader>
          <CardTitle>Consultar Organizaciones</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-2">
              IDs de Organizaciones
            </label>
            <textarea
              value={orgIds}
              onChange={(e) => setOrgIds(e.target.value)}
              placeholder="Ingrese los IDs separados por comas (ej: 1, 2, 3)"
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
              rows={3}
              disabled={loading}
            />
            <p className="text-sm text-gray-500 mt-1">
              Ejemplo: 1, 2, 3 o 1,2,3
            </p>
          </div>

          {error && (
            <div className="flex items-center space-x-2 p-3 bg-red-50 border border-red-200 rounded-md">
              <AlertCircle className="h-5 w-5 text-red-600 flex-shrink-0" />
              <span className="text-sm text-red-700">{error}</span>
            </div>
          )}

          <div className="flex space-x-3">
            <Button
              onClick={handleConsultar}
              disabled={loading}
              className="flex items-center space-x-2"
            >
              {loading ? (
                <>
                  <Loader2 className="h-4 w-4 animate-spin" />
                  <span>Consultando...</span>
                </>
              ) : (
                <>
                  <Network className="h-4 w-4" />
                  <span>Consultar</span>
                </>
              )}
            </Button>

            <Button
              onClick={handleLimpiar}
              variant="outline"
              disabled={loading}
            >
              Limpiar
            </Button>
          </div>
        </CardContent>
      </Card>

      {/* Resultados */}
      {data && (
        <div className="space-y-6">
          {/* Resumen */}
          <Card>
            <CardHeader>
              <CardTitle>Resumen de Resultados</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-2 gap-4">
                <div className="flex items-center space-x-3 p-4 bg-blue-50 rounded-lg">
                  <Users className="h-8 w-8 text-blue-600" />
                  <div>
                    <p className="text-sm text-gray-600">Presidentes</p>
                    <p className="text-2xl font-bold text-blue-900">
                      {data.totalPresidentes || 0}
                    </p>
                  </div>
                </div>
                <div className="flex items-center space-x-3 p-4 bg-green-50 rounded-lg">
                  <Building2 className="h-8 w-8 text-green-600" />
                  <div>
                    <p className="text-sm text-gray-600">Organizaciones</p>
                    <p className="text-2xl font-bold text-green-900">
                      {data.totalOrganizaciones || 0}
                    </p>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Tabla de Presidentes */}
          {data.presidentes && data.presidentes.length > 0 && (
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <Users className="h-5 w-5" />
                  <span>Presidentes</span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          ID
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Nombre
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Dirección
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Teléfono
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          ID Organización
                        </th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                      {data.presidentes.map((presidente, index) => (
                        <tr
                          key={presidente.id || index}
                          className="hover:bg-gray-50"
                        >
                          <td className="px-4 py-3 text-sm">
                            {presidente.id || "-"}
                          </td>
                          <td className="px-4 py-3 text-sm font-medium">
                            {presidente.name || "-"}
                          </td>
                          <td className="px-4 py-3 text-sm">
                            {presidente.address || "-"}
                          </td>
                          <td className="px-4 py-3 text-sm">
                            {presidente.phone || "-"}
                          </td>
                          <td className="px-4 py-3 text-sm">
                            {presidente.organizationId || "-"}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </CardContent>
            </Card>
          )}

          {/* Tabla de Organizaciones */}
          {data.organizaciones && data.organizaciones.length > 0 && (
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center space-x-2">
                  <Building2 className="h-5 w-5" />
                  <span>Organizaciones</span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="overflow-x-auto">
                  <table className="w-full">
                    <thead className="bg-gray-50">
                      <tr>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          ID
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Nombre
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Dirección
                        </th>
                        <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">
                          Teléfono
                        </th>
                      </tr>
                    </thead>
                    <tbody className="divide-y divide-gray-200">
                      {data.organizaciones.map((org, index) => (
                        <tr key={org.id || index} className="hover:bg-gray-50">
                          <td className="px-4 py-3 text-sm">{org.id || "-"}</td>
                          <td className="px-4 py-3 text-sm font-medium">
                            {org.name || "-"}
                          </td>
                          <td className="px-4 py-3 text-sm">
                            {org.address || "-"}
                          </td>
                          <td className="px-4 py-3 text-sm">
                            {org.phone || "-"}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </CardContent>
            </Card>
          )}

          {/* Mensaje cuando no hay resultados */}
          {(!data.presidentes || data.presidentes.length === 0) &&
            (!data.organizaciones || data.organizaciones.length === 0) && (
              <Card>
                <CardContent className="py-8">
                  <div className="text-center text-gray-500">
                    <AlertCircle className="h-12 w-12 mx-auto mb-2 text-gray-400" />
                    <p>No se encontraron resultados para los IDs ingresados</p>
                  </div>
                </CardContent>
              </Card>
            )}
        </div>
      )}
    </div>
  );
}
