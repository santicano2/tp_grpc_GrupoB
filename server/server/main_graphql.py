from fastapi import FastAPI
from strawberry.fastapi import GraphQLRouter
from .graphql.schema import schema

app = FastAPI()

# Mount GraphQL endpoint
graphql_app = GraphQLRouter(schema)
app.include_router(graphql_app, prefix="/graphql")

if __name__ == "__main__":
	import uvicorn
	print("[INFO] FastAPI GraphQL server running at http://localhost:8000/graphql")
	uvicorn.run("server.main_graphql:app", host="0.0.0.0", port=8000, reload=True)
