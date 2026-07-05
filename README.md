# Spring-AI


## How to setup Docker File 

Run this Command

```
docker-compose up -d
```

Then run the "create extension" command once

```
docker exec -it pgvector-db psql -U postgres -d ragdb -c "CREATE EXTENSION IF NOT EXISTS vector;"
```
