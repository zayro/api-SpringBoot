---
name: experto-java-reactivo-springboot-hexagonal
description: Guía para diseñar e implementar aplicaciones Java reactivas con Spring Boot reciente, arquitectura hexagonal y Clean Code.
---

# Experto en Java reactivo y Spring Boot

## Cuándo usar esta skill
Utiliza esta skill cuando necesites:
- Diseñar o refactorizar servicios backend en Java 17 o 21 con Spring Boot 3.x.
- Implementar programación reactiva con Project Reactor, WebFlux, R2DBC o mensajería reactiva.
- Organizar el código con arquitectura hexagonal, puertos y adaptadores.
- Mejorar la calidad del código con principios de Clean Code y patrones sólidos.

## Principios que deben guiar la solución
- Prioriza la programación reactiva para sistemas intensivos en E/S y alta concurrencia.
- Mantén la lógica de negocio en el núcleo del dominio y evita acoplarla a frameworks.
- Separa claramente casos de uso, puertos, adaptadores e infraestructura.
- Aplica Clean Code: nombres claros, métodos pequeños, responsabilidades simples y código legible.
- Usa las capacidades más recientes de Spring Boot, pero siempre con compatibilidad y simplicidad.

## Flujo recomendado
1. Comprende el contexto del problema, los requisitos y las restricciones técnicas.
2. Define la arquitectura: dominio, casos de uso, puertos, adaptadores y contratos externos.
3. Elige la estrategia reactiva adecuada: WebFlux, Reactor, R2DBC, mensajería o integración con APIs externas.
4. Implementa desde el núcleo hacia la infraestructura, manteniendo el dominio independiente.
5. Asegura resiliencia: manejo de errores, timeouts, reintentos, backpressure y observabilidad.
6. Valida la solución con pruebas unitarias, de integración y criterios de calidad.
7. Entrega un resultado claro, documentado y preparado para evolucionar.

## Puntos de decisión
- Si la aplicación requiere alta concurrencia y operaciones no bloqueantes, prioriza un enfoque reactivo.
- Si el sistema depende de librerías bloqueantes, encapsúlalas en adaptadores y minimiza su impacto.
- Si la persistencia es relacional, considera R2DBC o un adaptador asíncrono compatible.
- Si el flujo es orientado a eventos, usa Reactor junto con mensajería y manejo de errores explícito.

## Criterios de calidad
- El dominio no depende directamente de Spring ni de otras capas de infraestructura.
- Los casos de uso son claros, pequeños y fáciles de probar.
- Los adaptadores manejan detalles externos como bases de datos, APIs o colas.
- No hay llamadas bloqueantes dentro de un flujo reactivo cuando se puede evitar.
- Hay pruebas para el flujo feliz, los errores y los límites del sistema.
- La solución incluye logging, métricas y trazabilidad adecuadas.

## Ejemplos de prompts útiles
- Diseña un microservicio reactivo con Spring Boot 3, arquitectura hexagonal y Clean Code.
- Refactoriza este servicio para separar dominio, aplicación e infraestructura.
- Convierte este endpoint a WebFlux sin introducir bloqueos innecesarios.
- Propón una estructura de carpetas para una API reactiva con casos de uso claros.
- Mejora esta implementación aplicando principios SOLID, manejo de errores y pruebas.

## Buenas prácticas a aplicar
- Evita sobreingeniería cuando una solución simple sea suficiente.
- Mantén los modelos de dominio limpios y expresivos.
- Usa DTOs y contratos explícitos para separar la capa externa del núcleo.
- Documenta decisiones importantes de diseño y límites de contexto.
- Revisa continuamente rendimiento, escalabilidad y mantenibilidad.
