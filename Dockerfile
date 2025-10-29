FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copiar todos os arquivos do Gradle wrapper primeiro
COPY gradle/ ./gradle/
COPY gradlew ./
COPY build.gradle settings.gradle ./

# Dar permissão de execução ao gradlew
RUN chmod +x ./gradlew

# Baixar dependências
RUN ./gradlew dependencies --no-daemon

# Copiar código fonte
COPY src ./src

# Construir aplicação
RUN ./gradlew build -x test --no-daemon

# Stage 2: Run the application
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Instalar curl para health checks
RUN apk add --no-cache curl

# Criar usuário não-root
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copiar JAR construído
COPY --from=build /app/build/libs/*.jar mottu-spot.jar

# Dar permissão de leitura para o JAR
RUN chown appuser:appgroup mottu-spot.jar

# Mudar para usuário não-root
USER appuser

# Definir variáveis de ambiente padrão
ENV SPRING_PROFILES_ACTIVE=prod
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

EXPOSE 8080

# Usar ENTRYPOINT com JAVA_OPTS
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar mottu-spot.jar"]
