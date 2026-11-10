# students-api

CRUD de estudiantes con **DTO**, Spring Boot 4.1.2, Spring Data JPA y MySQL 8, con documentación Swagger y manejo global de excepciones.

Este proyecto continúa desde la versión del módulo 04 y aplica la refactorización del módulo 05: el servicio ya no devuelve `ServiceResult`, sino que lanza excepciones propias y un único `@RestControllerAdvice` convierte esos errores en respuestas HTTP consistentes.

## Requisitos

- Java 21 o superior
- Maven 3.9+ (o el wrapper `./mvnw`)
- MySQL 8 con la base `academy`

```bash
docker run -d --name academy-mysql \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_DATABASE=academy \
  -p 3306:3306 \
  mysql:8.0
```

## Ejecutar

```bash
./mvnw spring-boot:run
```

| Qué | Dirección |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| Contrato OpenAPI (JSON) | http://localhost:8080/api-docs |
| Contrato OpenAPI (YAML) | http://localhost:8080/api-docs.yaml |
| Contrato de un grupo | http://localhost:8080/api-docs/students · `/api-docs/internal` |
| API | http://localhost:8080/api/v1/students |
| Seed de ejemplo | `POST http://localhost:8080/internal/seed` |

## Casos de error que maneja la API

La API responde con un mismo formato JSON para todo fallo:

```json
{
  "timestamp": "2026-11-01T10:15:30.123Z",
  "status": 404,
  "error": "Not Found",
  "message": "No existe un estudiante con id 999",
  "path": "/api/v1/students/999"
}
```

Tipos cubiertos:

- `404` para estudiante no encontrado
- `409` para correo ya registrado
- `400` para validación, JSON inválido, tipo equivocado, parámetros faltantes o sort inválido
- `500` solo para fallos inesperados reales, con trazas en el log

## Estructura actual

```text
src/main/java/com/academy
├── AcademyApplication.java
├── business
│   ├── exception
│   │   ├── StudentNotFoundException.java
│   │   └── StudentEmailAlreadyExistsException.java
│   ├── impl
│   │   └── StudentServiceImpl.java
│   └── service
│       └── StudentService.java
├── persistence
│   ├── entity
│   │   ├── StudentEntity.java
│   │   └── StudentStatus.java
│   └── repository
│       └── StudentJpaRepository.java
├── presentation
│   ├── config
│   │   └── OpenApiConfig.java
│   ├── controller
│   │   ├── StudentController.java
│   │   └── InternalController.java
│   ├── dto
│   │   ├── ErrorResponse.java
│   │   ├── StudentRequest.java
│   │   ├── StudentStatusRequest.java
│   │   ├── StudentResponse.java
│   │   └── PageResponse.java
│   ├── exception
│   │   └── GlobalExceptionHandler.java
│   └── mapper
│       └── StudentMapper.java
└── resources
    ├── application.properties
    ├── application-prod.properties
    └── migrate.sql
```

## Pruebas

```bash
./mvnw test -q
```

## Sin documentación (producción)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

## Notas

- Los controladores quedan limpios y solo manejan el camino feliz.
- El formato de error está centralizado con `@RestControllerAdvice`.
- La capa de negocio no conoce HTTP ni genera respuestas; solo lanza excepciones con mensajes claros.
- Los mensajes de validación están en español para que la API responda de forma consistente.


