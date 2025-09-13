const Table = ({ children }) => {
  return (
    <div className="overflow-x-auto">
      <table className="min-w-full divide-y divide-gray-200">{children}</table>
    </div>
  );
};

const TableHeader = ({ children }) => {
  return <thead className="bg-gray-50">{children}</thead>;
};

const TableBody = ({ children }) => {
  return (
    <tbody className="bg-white divide-y divide-gray-200">{children}</tbody>
  );
};

const TableRow = ({ children, className = "" }) => {
  return <tr className={`hover:bg-gray-50 ${className}`}>{children}</tr>;
};

const TableHead = ({ children, className = "" }) => {
  return (
    <th
      className={`px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider ${className}`}
    >
      {children}
    </th>
  );
};

const TableData = ({ children, className = "" }) => {
  return (
    <td
      className={`px-6 py-4 whitespace-nowrap text-sm text-gray-900 ${className}`}
    >
      {children}
    </td>
  );
};

export { Table, TableHeader, TableBody, TableRow, TableHead, TableData };
