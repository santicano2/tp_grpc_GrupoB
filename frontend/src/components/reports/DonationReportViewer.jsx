import { useQuery, gql } from '@apollo/client';
import { useState } from 'react';

const DONATION_REPORT_QUERY = gql`
  query DonationReport($actorUsername: String!, $filter: DonationReportFilter) {
    donation_report(actorUsername: $actorUsername, filter: $filter) {
      summary {
        category
        deleted
        total_quantity
      }
      details {
        id
        category
        description
        quantity
        deleted
        created_at
        created_by
        updated_at
        updated_by
      }
    }
  }
`;

export default function DonationReportViewer({ actorUsername }) {
  const [category, setCategory] = useState("");
  const [deleted, setDeleted] = useState("");
  const { data, loading, error, refetch } = useQuery(DONATION_REPORT_QUERY, {
    variables: {
      actorUsername,
      filter: {
        category: category || undefined,
        deleted: deleted === "" ? undefined : deleted === "true",
      },
    },
    fetchPolicy: 'network-only',
  });

  return (
    <div>
      <h2>Informe de Donaciones</h2>
      <div style={{ marginBottom: 16 }}>
        <label>
          Categoría:
          <input value={category} onChange={e => setCategory(e.target.value)} placeholder="ROPA, ALIMENTOS..." />
        </label>
        <label style={{ marginLeft: 16 }}>
          Eliminado:
          <select value={deleted} onChange={e => setDeleted(e.target.value)}>
            <option value="">Todos</option>
            <option value="true">Sí</option>
            <option value="false">No</option>
          </select>
        </label>
        <button onClick={() => refetch()}>Filtrar</button>
      </div>
      {loading && <p>Cargando...</p>}
      {error && <p style={{ color: 'red' }}>{error.message}</p>}
      {data && (
        <>
          <h3>Resumen</h3>
          <table border="1" cellPadding="4">
            <thead>
              <tr>
                <th>Categoría</th>
                <th>Eliminado</th>
                <th>Total</th>
              </tr>
            </thead>
            <tbody>
              {data.donation_report.summary.map((row, i) => (
                <tr key={i}>
                  <td>{row.category}</td>
                  <td>{row.deleted ? "Sí" : "No"}</td>
                  <td>{row.total_quantity}</td>
                </tr>
              ))}
            </tbody>
          </table>
          <h3>Detalle</h3>
          <table border="1" cellPadding="4">
            <thead>
              <tr>
                <th>ID</th>
                <th>Categoría</th>
                <th>Descripción</th>
                <th>Cantidad</th>
                <th>Eliminado</th>
                <th>Creado</th>
                <th>Por</th>
                <th>Actualizado</th>
                <th>Por</th>
              </tr>
            </thead>
            <tbody>
              {data.donation_report.details.map((row) => (
                <tr key={row.id}>
                  <td>{row.id}</td>
                  <td>{row.category}</td>
                  <td>{row.description}</td>
                  <td>{row.quantity}</td>
                  <td>{row.deleted ? "Sí" : "No"}</td>
                  <td>{row.created_at}</td>
                  <td>{row.created_by}</td>
                  <td>{row.updated_at || "-"}</td>
                  <td>{row.updated_by || "-"}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </>
      )}
    </div>
  );
}
