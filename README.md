# kingfisher

![GitHub Release](https://img.shields.io/github/v/release/awesomengwin/kingfisher)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/awesomengwin/kingfisher/ci.yml)

## Development

Run Postgres Docker container

```shell
docker run --rm \
-e POSTGRES_DB=kingfisher \
-e POSTGRES_USER=kingfisher \
-e POSTGRES_PASSWORD=kingfisher \
-p 5432:5432 postgres
```

Run app with Docker

```shell
export SPOTIFY_CLIENT_ID=your_spotify_client_id
export SPOTIFY_CLIENT_SECRET=your_spotify_client_secret
export LYRICS_API_URL=your_lyrics_api_url
```

```shell
docker run --rm \
-e SPOTIFY_CLIENT_ID="$SPOTIFY_CLIENT_ID" \
-e SPOTIFY_CLIENT_SECRET="$SPOTIFY_CLIENT_SECRET" \
-e LYRICS_API_URL="$LYRICS_API_URL" \
-e DB_URL=jdbc:postgresql://host.docker.internal:5432/kingfisher \
-p 8080:8080 ghcr.io/awesomengwin/kingfisher:latest
```
