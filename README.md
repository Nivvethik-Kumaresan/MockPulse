# MockPulse

Collaborative Real-Time API Mocking & Webhook Tester.

## Monorepo Structure

- `backend/` — Spring Boot 3.x API mock server
- `frontend/` — React + Vite UI
- `docker-compose.yml` — local MongoDB instance

## Architecture Overview

### Backend (Spring Boot)

- Dynamic request dispatcher (`/**`) routes incoming requests against stored `MockRoute` records.
- Latency simulation service applies route-specific `delayMs` before responding.
- JSON templating service normalizes configured JSON payloads.
- WebSocket endpoint at `/ws/webhooks` broadcasts webhook payloads in real-time.
- Webhook capture endpoint (`/webhooks/**`) accepts any webhook payload and forwards it to WebSocket clients.

#### `MockRoute` MongoDB document

Fields:
- `id`
- `path`
- `httpMethod`
- `responseStatus`
- `responseHeaders`
- `responseBody`
- `delayMs`

> Add a compound MongoDB index on `path + httpMethod` for fast route lookup.

### Frontend (React + Vite + Tailwind)

- API Endpoint Builder form for path, HTTP method, status code, delay, and JSON response.
- Built-in JSON validation before save.
- Real-time Webhook Inspector that listens to backend WebSocket events.

## Local Setup

### 1) Start MongoDB

```bash
docker compose up -d
```

### 2) Run backend

```bash
cd backend
mvn spring-boot:run
```

### 3) Run frontend

```bash
cd frontend
npm install
npm run dev
```

## Default Local Endpoints

- Frontend: `http://localhost:5173`
- Backend API: `http://localhost:8080`
- WebSocket: `ws://localhost:8080/ws/webhooks`
- MongoDB: `mongodb://localhost:27017/mockpulse`
