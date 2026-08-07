# kingfisher

## Development

Run Postgres Docker container

```shell
docker run --rm -e POSTGRES_DB=kingfisher -e POSTGRES_USER=kingfisher -e POSTGRES_PASSWORD=kingfisher -p 5432:5432 postgres
```

Run Redis Docker container

```shell
docker run --rm -p 6379:6379 redis
```
