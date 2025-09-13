# Frontend - Empuje Comunitario

Frontend desarrollado con React + Vite para el sistema de gestión de ONG.

## Requisitos previos

- Node.js 16+
- npm
- API Gateway ejecutándose en puerto 8080
- Servidor gRPC Python ejecutándose en puerto 50051

## Instalación

```bash
npm install
```

## Ejecución

```bash
npm run dev
```

La aplicación se iniciará en `http://localhost:5173`

## Usuarios de prueba

| Usuario      | Contraseña  | Rol         |
| ------------ | ----------- | ----------- |
| presidente1  | password123 | PRESIDENTE  |
| vocal1       | password123 | VOCAL       |
| vocal2       | password123 | VOCAL       |
| coordinador1 | password123 | COORDINADOR |
| coordinador2 | password123 | COORDINADOR |
| voluntario1  | password123 | VOLUNTARIO  |
| voluntario2  | password123 | VOLUNTARIO  |
| voluntario3  | password123 | VOLUNTARIO  |

## Arquitectura

```
Frontend (React) → API Gateway (Spring Boot) → Servidor gRPC (Python) → MySQL
     :5173              :8080                      :50051            :3307
```

## Funcionalidades implementadas

### Autenticación

- Login con usuarios reales de la base de datos
- Manejo de estados de autenticación
- Validación de credenciales

### Servicios API

- Servicio de autenticación (login)
- Servicio de usuarios (crear)
- Servicio de eventos (listar, crear, asignar miembros)
- Servicio de donaciones (listar, crear)

---

## Stack principal

- React
- TailwindCSS

---

---

### TODO

- Hacer gestión de usuarios y conectar a API (datos del server)
- Conectar gestion de inventario a API (datos del server)
- Conectar gestion de eventos a API (datos del server)

---
