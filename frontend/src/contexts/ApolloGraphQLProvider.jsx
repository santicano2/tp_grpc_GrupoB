import { ApolloClient, InMemoryCache, ApolloProvider } from '@apollo/client';

// Cambia la URL si tu backend GraphQL corre en otro host/puerto
const client = new ApolloClient({
  uri: 'http://localhost:8000/graphql',
  cache: new InMemoryCache(),
});

export function ApolloGraphQLProvider({ children }) {
  return <ApolloProvider client={client}>{children}</ApolloProvider>;
}
