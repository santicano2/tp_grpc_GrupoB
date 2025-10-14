import { StrictMode } from "react";
import { createRoot } from "react-dom/client";

import App from "./App.jsx";
import "./index.css";
import { ApolloGraphQLProvider } from "./contexts/ApolloGraphQLProvider.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <ApolloGraphQLProvider>
      <App />
    </ApolloGraphQLProvider>
  </StrictMode>
);
