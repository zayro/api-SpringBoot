# Diseño propuesto de microservicio reactivo

## Arquitectura
- Dominio: modelos y puertos de negocio.
- Aplicación: casos de uso y servicios de consulta.
- Infraestructura: adaptadores WebFlux y repositorio en memoria.

## Endpoints
- GET /customers
- GET /customers/{id}

## Principios aplicados
- Reactor y WebFlux para un flujo no bloqueante.
- Separación de responsabilidades por capas.
- Clean Code con nombres claros y clases pequeñas.
- Arquitectura hexagonal con dependencias dirigidas al dominio.
