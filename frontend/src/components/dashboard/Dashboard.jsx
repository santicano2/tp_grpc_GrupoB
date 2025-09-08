import { Users, Package, Calendar, AlertCircle } from "lucide-react";

import { useAuth } from "../../contexts/AuthContext";
import { mockUsers, mockInventory, mockEvents } from "../../data/mockData";

const Dashboard = () => {
  const { user } = useAuth();

  return <div>Hola</div>;
};

export default Dashboard;
