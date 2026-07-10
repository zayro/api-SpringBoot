# Contexto del Proyecto - API Spring Boot Reactiva

## 📋 Descripción General

API REST reactiva basada en **Spring Boot 4.1.0** con arquitectura hexagonal y Clean Code, implementando:
- CRUD completo de productos (base de datos y en memoria)
- Autenticación JWT con login
- Validación de negocio centralizada
- Logs globales y manejo de errores
- Perfiles de desarrollo y producción
- Versionado de API (`/api/v1/`)

**Fecha de actualización:** 2026-07-08

---

## 🏗️ Arquitectura

### Estructura Hexagonal (Ports & Adapters)

```
com.rest.api/
├── domain/
│   ├── model/
│   │   └── Product.java                    # Modelo de dominio (Lombok @Value @Builder)
│   └── port/
│       └── ProductRepository.java          # Puerto (interfaz)
├── application/
│   ├── service/
│   │   ├── ProductService.java             # Caso de uso CRUD con validación
│   │   └── ProductRawService.java          # Caso de uso raw SQL
│   └── validation/
│       └── ProductPriceValidator.java      # Validación de negocio
├── infrastructure/
│   ├── adapter/
│   │   ├── input/web/
│   │   │   ├── ProductController.java      # REST estándar
│   │   │   ├── ProductRawController.java   # REST raw SQL
│   │   │   ├── AuthController.java         # Autenticación
│   │   │   ├── AuthRequest.java
│   │   │   ├── AuthResponse.java
│   │   │   └── ProductRawResponse.java
│   │   └── output/r2dbc/
│   │       ├── ProductEntity.java          # Entidad JPA
│   │       ├── ProductR2dbcRepository.java # Repositorio Spring Data R2DBC
│   │       └── ProductRepositoryR2dbcAdapter.java
│   │   └── inmemory/
│   │       └── InMemoryProductRepository.java
│   ├── config/
│   │   ├── SecurityConfig.java             # Configuración de seguridad
│   │   ├── LoggingWebFilter.java           # Filtro global de logs
│   │   └── GlobalExceptionHandler.java     # Manejo centralizado de errores
│   ├── mapper/
│   │   └── ProductMapper.java              # Mapeos domain ↔ entity
│   └── security/
│       ├── JwtService.java                 # Generación y validación JWT
│       └── JwtAuthenticationFilter.java    # Filtro de autenticación
└── ApiApplication.java
```

---

## 🔧 Stack Tecnológico

| Componente | Versión | Propósito |
|-----------|---------|----------|
| **Spring Boot** | 4.1.0 | Framework base |
| **WebFlux** | Reactivo | APIs no-bloqueantes |
| **R2DBC + PostgreSQL** | Reactivo | Acceso a base de datos |
| **Spring Security** | 6.x | Autenticación/Autorización |
| **JWT (jjwt)** | 0.12.6 | Tokens seguros |
| **Lombok** | 1.18.26 | Reducción de boilerplate |
| **Jakarta Validation** | 3.x | Validación de datos |

---

## 📦 Dependencias Principales

```gradle
// Reactive web
implementation 'org.springframework.boot:spring-boot-starter-webflux'

// Security & JWT
implementation 'org.springframework.boot:spring-boot-starter-security'
implementation 'io.jsonwebtoken:jjwt-api:0.12.6'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.6'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.6'

// Database - Reactive
implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc'
runtimeOnly 'org.postgresql:r2dbc-postgresql'

// Validation & Logs
implementation 'org.springframework.boot:spring-boot-starter-validation'
implementation 'org.springframework.boot:spring-boot-starter-actuator'

// Lombok
compileOnly 'org.projectlombok:lombok:1.18.26'
annotationProcessor 'org.projectlombok:lombok:1.18.26'
```

---

## 🌐 Endpoints

### Autenticación (Público)

#### POST `/auth/login`
Obtiene JWT token para proteger operaciones de escritura.

**Request:**
```json
{
  "username": "admin",
  "password": "admin"
}
```

**Response (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "expiresIn": 86400
}
```

**Credenciales demo:** `admin`/`admin`, `user`/`user`

---

### Productos - Rutas Estándar (Mapeo con DTO)

#### GET `/api/products` o `/api/v1/products`
Lista todos los productos (público).

**Response (200):**
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop",
    "price": 1200.50
  }
]
```

#### GET `/api/products/{id}` o `/api/v1/products/{id}`
Obtiene un producto (público).

**Response (200):**
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop",
  "price": 1200.50
}
```

**Response (404):** Not Found

---

#### POST `/api/products` o `/api/v1/products` ⚠️ **Protegido**
Crea un producto. Requiere: `Authorization: Bearer <token>`

**Request:**
```json
{
  "name": "Monitor",
  "description": "4K Ultra HD",
  "price": 599.99
}
```

**Response (201):**
```json
{
  "id": 2,
  "name": "Monitor",
  "description": "4K Ultra HD",
  "price": 599.99
}
```

**Errores:**
- 400: Validación fallida (precio > 1,000,000 o campos vacíos)
- 401: Sin token o token inválido

---

#### PUT `/api/products/{id}` ⚠️ **Protegido**
Actualiza un producto. Requiere: `Authorization: Bearer <token>`

**Request:**
```json
{
  "name": "Monitor Updated",
  "description": "4K Ultra HD",
  "price": 549.99
}
```

**Response (200):** Producto actualizado

**Errores:**
- 400: Validación fallida
- 401: Sin autenticación
- 404: Producto no encontrado

---

#### DELETE `/api/products/{id}` ⚠️ **Protegido**
Elimina un producto. Requiere: `Authorization: Bearer <token>`

**Response (204):** No Content

**Errores:**
- 401: Sin autenticación
- 404: Producto no encontrado

---

### Productos - Raw SQL (Acceso directo a queries)

#### GET `/api/products/raw`
Ejecuta query SQL visible y devuelve productos con SQL metadata.

**Response (200):**
```json
{
  "sql": "SELECT id, name, description, price FROM products ORDER BY id",
  "result": [
    {
      "id": 1,
      "name": "Laptop",
      "description": "High-performance laptop",
      "price": 1200.50
    }
  ]
}
```

#### GET `/api/products/raw/{id}`
Obtiene un producto con raw SQL.

#### POST `/api/products/raw` ⚠️ **Protegido**
Inserta con SQL visible.

**Request:**
```json
{
  "name": "Keyboard",
  "description": "Mechanical",
  "price": 150.00
}
```

**Response (200):**
```json
{
  "sql": "INSERT INTO products...",
  "result": { "id": 3 }
}
```

#### PUT `/api/products/raw/{id}` ⚠️ **Protegido**
Actualiza con SQL visible.

**Response (200):**
```json
{
  "sql": "UPDATE products...",
  "result": { "rowsUpdated": 1 }
}
```

#### DELETE `/api/products/raw/{id}` ⚠️ **Protegido**
Elimina con SQL visible.

**Response (200):**
```json
{
  "sql": "DELETE FROM products...",
  "result": { "rowsDeleted": 1 }
}
```

---

## 🔐 Autenticación JWT

### Flujo

1. **POST `/auth/login`** con credenciales → obtiene `token`
2. **Incluir en header:** `Authorization: Bearer <token>`
3. El filtro `JwtAuthenticationFilter` valida y establece contexto de seguridad
4. El servicio procede si token es válido

### Configuración

**`application.properties`:**
```properties
jwt.secret=${JWT_SECRET:my-super-secret-key-...}  # Cambia en producción
jwt.expiration=${JWT_EXPIRATION:86400000}         # 24 horas
```

### Protección de Endpoints

- ✅ **GET** `/api/**` → Público
- 🔒 **POST** `/api/**` → Requiere JWT
- 🔒 **PUT** `/api/**` → Requiere JWT
- 🔒 **DELETE** `/api/**` → Requiere JWT
- ✅ **POST** `/auth/login` → Público

---

## ✅ Validaciones de Negocio

### Validación de Precio

**Regla:** El precio no puede exceder `1,000,000`

**Implementación:**
- Clase: `ProductPriceValidator` (capa de aplicación)
- Anotación en DTO: `@DecimalMax("1000000")`
- Lanzamiento: `IllegalArgumentException` → HTTP 400

**Flujo:**
1. Controller recibe request
2. Jakarta Validation valida DTO (`@NotNull`, `@PositiveOrZero`, `@DecimalMax`)
3. Service inyecta `ProductPriceValidator` y valida regla de negocio
4. `GlobalExceptionHandler` captura `IllegalArgumentException` → HTTP 400

**Ejemplo Error (400):**
```json
{
  "timestamp": "2026-07-08T10:30:45Z",
  "error": "Product price cannot exceed 1000000"
}
```

---

## 🛠️ Configuración de Perfiles

### Desarrollo (`dev`)

**Archivo:** `application-dev.properties`
```properties
server.port=8080
logging.level.root=INFO
logging.level.com.rest.api=DEBUG
```

**Activación:**
```properties
spring.profiles.active=dev
```

### Producción (`prod`)

**Archivo:** `application-prod.properties`
```properties
server.port=${SERVER_PORT:8080}
logging.level.root=INFO
logging.level.com.rest.api=WARN
spring.r2dbc.url=${DB_R2DBC_URL}
spring.r2dbc.username=${DB_USERNAME}
spring.r2dbc.password=${DB_PASSWORD}
```

**Activación en Deploy:**
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_R2DBC_URL=r2dbc:postgresql://prod-host:5432/db
export DB_USERNAME=produser
export DB_PASSWORD=prodpass
export JWT_SECRET=your-production-secret-key
```

---

## 📝 Base de Datos

### Conexión

**PostgreSQL R2DBC (reactivo):**
```properties
spring.r2dbc.url=r2dbc:postgresql://localhost:5432/enterprise
spring.r2dbc.username=postgres
spring.r2dbc.password=zayro
```

### Esquema

**SQL de creación:**

```sql
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(19, 2) NOT NULL CHECK (price >= 0 AND price <= 1000000),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO products (name, description, price) VALUES
  ('Laptop', 'High-performance laptop', 1200.50),
  ('Mouse', 'Wireless mouse', 45.00),
  ('Keyboard', 'Mechanical keyboard', 150.00);
```

**Archivo de script:**
```
src/main/resources/db/schema-products.sql
```

---

## 🚀 Cómo Ejecutar

### Requisitos

- Java 17+
- PostgreSQL 12+
- Gradle 9.5+

### Pasos

1. **Clonar repositorio:**
   ```bash
   cd d:\GitHub\api-SpringBoot
   ```

2. **Crear base de datos PostgreSQL:**
   ```sql
   CREATE DATABASE enterprise;
   -- Ejecutar schema-products.sql
   ```

3. **Compilar:**
   ```bash
   .\gradlew.bat compileJava
   ```

4. **Ejecutar:**
   ```bash
   .\gradlew.bat bootRun
   ```

5. **Acceso:**
   - API: `http://localhost:8080`
   - Logs: Console con nivel DEBUG

---

## 🔍 Flujos de Ejemplo

### 1. Crear Producto (Completo)

```bash
# 1. Autenticarse
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

# Respuesta: { "token": "eyJhbGc...", "username": "admin", "expiresIn": 86400 }

# 2. Crear producto con token
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer eyJhbGc..." \
  -H "Content-Type: application/json" \
  -d '{"name":"Monitor","description":"4K","price":599.99}'

# Respuesta: { "id": 4, "name": "Monitor", "description": "4K", "price": 599.99 }
```

### 2. Listar Productos (Público)

```bash
curl -X GET http://localhost:8080/api/products
```

### 3. Ver Query Raw

```bash
curl -X GET http://localhost:8080/api/products/raw
```

---

## 📊 Logs

### Filtro Global

**`LoggingWebFilter`:**
- Registra método, ruta, tiempo de respuesta
- Captura errores no manejados

**Ejemplo:**
```
INFO  - Incoming request: GET /api/products
INFO  - Response for GET /api/products completed in 45 ms
ERROR - Error for POST /api/products after 23 ms
```

### Niveles por Perfil

| Perfil | Root | com.rest.api |
|--------|------|--------------|
| dev    | INFO | DEBUG        |
| prod   | INFO | WARN         |

---

## 🎯 Decisiones de Diseño

### 1. Arquitectura Hexagonal
- **Por qué:** Separación clara entre lógica de negocio y adaptadores (DB, Web, etc.)
- **Beneficio:** Fácil de testear, reemplazar adaptadores sin afectar dominio

### 2. Validación en Capas
- **DTO:** Restricciones básicas (`@NotNull`, `@NotBlank`, `@DecimalMax`)
- **Servicio:** Reglas de negocio (`ProductPriceValidator`)
- **Beneficio:** Validación robusta y coherente

### 3. Mapper Centralizado
- **Por qué:** Un único punto de mapeo entre entity, domain, DTO
- **Beneficio:** Evita duplicación y cambios coherentes

### 4. JWT Stateless
- **Por qué:** API escalable sin sesiones server
- **Beneficio:** Microservicios pueden verificar token sin estado compartido

### 5. WebFlux Reactivo
- **Por qué:** APIs no-bloqueantes con menos threads
- **Beneficio:** Mayor throughput, menos recursos para conexiones concurrentes

---

## 🔮 Próximos Pasos (Recomendados)

- [ ] Agregar pruebas unitarias (JUnit 5 + Mockito)
- [ ] Agregar pruebas de integración (WebTestClient)
- [ ] Implementar auditoría (created_at, updated_at, created_by)
- [ ] Agregar roles de usuario (ROLE_USER, ROLE_ADMIN)
- [ ] Refresh tokens para JWT
- [ ] Rate limiting (Spring Cloud Gateway)
- [ ] Documentación OpenAPI/Swagger
- [ ] Cache (Spring Cache, Redis)
- [ ] Métricas (Micrometer, Prometheus)

---

## 📞 Contacto de Desarrollo

**Último actualizado:** 2026-07-08  
**Versión:** 0.0.1-SNAPSHOT
