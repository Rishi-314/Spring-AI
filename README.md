# Spring-AI


## How to setup Docker File 

This project uses PostgreSQL with the `pgvector` extension as the vector store for RAG.

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/) installed and running

### Steps

1. Start the Postgres + pgvector container:
```bash
   docker-compose up -d
```

2. Enable the `vector` extension (only needed once, on first setup):
```bash
   docker exec -it pgvector-db psql -U postgres -d ragdb -c "CREATE EXTENSION IF NOT EXISTS vector;"
```

3. Verify the container is running:
```bash
   docker ps
```
   You should see `pgvector-db` listed with status "Up".

### Notes
- Database is exposed on `localhost:5433` (mapped to avoid conflicts with any local Postgres install on the default `5432` port).
- Credentials: `postgres` / `postgres`, database name `ragdb` (see `docker-compose.yml`).
- Data persists across restarts via a named Docker volume (`pgvector-data`).
