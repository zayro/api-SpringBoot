 # Proyecto: api-SpringBoot (microservicio reactivo)

 ## Objetivo

 - Proveer un microservicio con Spring Boot 4 usando WebFlux y R2DBC (PostgreSQL).
 - Arquitectura hexagonal: dominio, puertos, adaptadores, y servicios de aplicación.
 - Ejemplo CRUD sobre la tabla `products` y un repositorio en memoria.

 ## Archivos añadidos importantes

 - `src/main/java/com/rest/api/domain/model/Product.java`
 - `src/main/java/com/rest/api/domain/port/ProductRepository.java`
 - `src/main/java/com/rest/api/application/service/ProductService.java`
 - `src/main/java/com/rest/api/infrastructure/adapter/output/r2dbc/*`
 - `src/main/java/com/rest/api/infrastructure/adapter/output/inmemory/InMemoryProductRepository.java`
 - `src/main/java/com/rest/api/infrastructure/adapter/input/web/ProductController.java`
 - `db/schema-products.sql` (script para PostgreSQL)
 - `src/main/java/com/rest/api/application/service/ProductRawService.java`
 - `src/main/java/com/rest/api/infrastructure/adapter/input/web/ProductRawController.java`

Cómo ejecutar la base de datos (local)
1. Crear la base de datos `enterprise` en PostgreSQL y ejecutar el script:

```bash
psql -U postgres -c "CREATE DATABASE enterprise;"
psql -U postgres -d enterprise -f db/schema-products.sql
```

2. Variables de conexión (ver `src/main/resources/application.properties`):
- `spring.r2dbc.url=r2dbc:postgresql://localhost:5432/enterprise`
- `spring.r2dbc.username` y `spring.r2dbc.password`

Cómo ejecutar la aplicación
- Asegúrate de tener `JAVA_HOME` configurado (JDK 17+ o según toolchain en `build.gradle`).

```bash
./gradlew bootRun
```

Endpoints útiles (CRUD y consultas raw)
- CRUD estándar JSON (usa `ProductController`):
  - `GET /api/products`
  - `GET /api/products/{id}`
  - `POST /api/products` (body: `{ "name":"...","description":"...","price":12.34 }`)
  - `PUT /api/products/{id}`
  - `DELETE /api/products/{id}`

- CRUD raw con SQL visible (usa `ProductRawController`):
  - `GET /api/products/raw` -> devuelve SQL y lista de filas
  - `GET /api/products/raw/{id}` -> devuelve SQL y fila
  - `POST /api/products/raw` -> ejecuta INSERT (devuelve SQL y id)
  - `PUT /api/products/raw/{id}` -> ejecuta UPDATE (devuelve SQL y rowsUpdated)
  - `DELETE /api/products/raw/{id}` -> ejecuta DELETE (devuelve SQL y rowsUpdated)

Notas
- El script `db/schema-products.sql` es para ejecución manual.
- Actualmente el proyecto incluye tanto el adaptador R2DBC como un repositorio en memoria para pruebas locales.
- Si quieres, puedo agregar profiles `dev`/`prod` para elegir repositorio por defecto.
