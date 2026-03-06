# Shifts API

Backend en Spring Boot para gestionar turnos en una tienda. Permite crear una cola, llamar turnos por ventanilla, marcarlos como atendidos y recibir actualizaciones en tiempo real por WebSocket.

## Tecnologías

- Java 21
- Spring Boot 3.5.9
- Spring Web + Spring Data JPA + WebSocket (STOMP/SockJS)
- PostgreSQL 16
- Bucket4j (rate limiting)
- Maven
- Docker / Docker Compose

## Requisitos

- Java 21
- Maven 3.9+ (o usar `./mvnw`)
- PostgreSQL (si ejecutas sin Docker)
- Docker + Docker Compose (opcional)

## Variables de entorno

La aplicación usa estas variables:

- `DB_IP`
- `DB_PORT`
- `DB_NAME`
- `DB_USER`
- `DB_PASSWORD`
- `FRONT_URL`
- `SECURE_FRONT_URL`

Ejemplo (`.env`):

```env
DB_IP=localhost
DB_NAME=ticketQueue
DB_USER=postgres
DB_PASSWORD=root
DB_PORT=5432
FRONT_URL=http://localhost:5173/
SECURE_FRONT_URL=https://localhost:5173/
```

## Ejecutar localmente (sin Docker)

1. Levanta PostgreSQL.
2. Asegúrate de tener las variables de entorno configuradas (o archivo `.env`).
3. Ejecuta la API:

```bash
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

## Ejecutar con Docker Compose

```bash
docker compose up --build
```

Servicios:

- API: `http://localhost:8080`
- PostgreSQL: contenedor `shifts-db`

## Endpoints HTTP

Base URL: `http://localhost:8080`

### `POST /new`
Crea una nueva cola de turnos.

Body: sin body.

### `POST /call`
Bloquea el turno actual para una ventanilla y avanza al siguiente número.

Body:

```json
{
  "privateCode": "abc12345",
  "counterName": "Caja 1"
}
```

### `PUT /served`
Marca como atendido un ticket bloqueado.

Body:

```json
{
  "privateCode": "abc12345",
  "ticketId": "550e8400-e29b-41d4-a716-446655440000"
}
```

### `DELETE /delete`
Elimina manualmente un ticket bloqueado por ID.

Body:

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "privateCode": "abc12345"
}
```

### `PUT /edit`
Edita el número actual y rango de turnos.

Body:

```json
{
  "newCurrentNumber": 5,
  "newLowestNumber": 1,
  "newHighestNumber": 100,
  "privateCode": "abc12345"
}
```

### `GET /ticketQueue/{privateCode}`
Obtiene el estado actual de la cola.

## Formato de respuesta

La mayoría de endpoints devuelven:

```json
{
  "response": {
    "currentShiftNumber": 1,
    "privateCode": "abc12345",
    "lowestNumberOfShift": 1,
    "highestNumberOfShift": 100,
    "blockedTicketList": []
  }
}
```

## WebSocket en tiempo real

- Endpoint SockJS: `/ws`
- Topic por cola: `/topic/shifts/{privateCode}`

Cada cambio relevante (`/call`, `/served`, `/delete`, `/edit`) publica el estado actualizado de la cola en ese topic.

## Rate limiting

Se aplica por IP y endpoint:

- `/new`: 5 requests cada 10 segundos
- `/call`: 5 requests cada 1 segundo
- `/edit`: 5 requests cada 10 segundos
- `/ticketQueue/**`: 5 requests cada 10 segundos

Cuando se supera el límite, responde `429 Too Many Requests`.
