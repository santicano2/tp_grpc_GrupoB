import { ApolloClient, InMemoryCache, HttpLink } from "@apollo/client";
import { ApolloProvider } from "@apollo/client/react";

// Crear el link HTTP explicitamente
const httpLink = new HttpLink({
  uri: "http://localhost:8000/graphql",
});

// Configurar el cliente Apollo
const client = new ApolloClient({
  link: httpLink,
  cache: new InMemoryCache(),
});

export function ApolloGraphQLProvider({ children }) {
  return <ApolloProvider client={client}>{children}</ApolloProvider>;
}
