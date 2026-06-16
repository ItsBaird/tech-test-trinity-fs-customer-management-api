# STAGE 1: BUILD
# Compila el proyecto y genera el JAR
FROM maven:3.9.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copiamos primero solo el pom.xml para aprovechar el cache de capas.
# Si no cambian las dependencias, Docker reutiliza esta capa
# y no vuelve a descargar todo desde Maven Central.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Ahora copiamos el código fuente y compilamos
COPY src ./src
RUN mvn clean package -DskipTests -B

# STAGE 2: RUNTIME
# Imagen final liviana, sin Maven ni código fuente
FROM eclipse-temurin:21-jre-alpine AS runtime

# Usuario no-root por seguridad (nunca correr como root en producción)
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copiamos solo el JAR generado en el stage anterior
COPY --from=builder /app/target/customer-management-api-0.0.1-SNAPSHOT.jar app.jar

# Cambiamos al usuario sin privilegios
USER appuser

# Puerto expuesto (solo documentativo, docker-compose lo mapea)
EXPOSE 8080

# JVM tuning para contenedores:
# -XX:+UseContainerSupport  → detecta correctamente los límites de memoria del contenedor
# -XX:MaxRAMPercentage=75.0 → usa hasta el 75% de la RAM asignada al contenedor
ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-jar", "app.jar"]