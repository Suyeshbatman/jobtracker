# JobTracker (Backend)

Spring Boot + Postgres + Flyway + JWT

## Run locally
1. Start Postgres via Docker Compose
2. Run Spring Boot app
3. Test endpoints with Postman

## Endpoints
- POST /auth/register
- POST /auth/login
- GET /applications
- POST /applications
- PATCH /applications/{id}/status
- GET /applications/{id}/history
