# Backend Setup — Spring Boot

## Requisitos
- Java 21+
- Maven (o usar mvnw)

## Configuración

### Variables de entorno (.env)
```env
DB_URL=postgresql://postgres:PASSWORD@db.ieirtsxybbtvyqahlnhq.supabase.co:5432/postgres
SUPABASE_URL=https://ieirtsxybbtvyqahlnhq.supabase.co
SUPABASE_SERVICE_KEY=eyJhbGciOiJIUzI1NiIs...
JWT_SECRET=tu_jwt_secret_aqui
```

### Ejecutar
```bash
# Con Maven instalado
mvn spring-boot:run

# Sin Maven (usar wrapper)
./mvnw spring-boot:run

# Con perfil de desarrollo
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### API Documentation
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI: http://localhost:8080/v3/api-docs

## Estructura
```
backend/src/main/java/com/clinica/
├── ClinicaApplication.java
├── config/
├── security/
├── exception/
├── shared/
└── module/
    ├── auth/
    ├── usuario/
    ├── paciente/
    ├── medico/
    ├── especialidad/
    ├── turno/
    └── archivo/
```
