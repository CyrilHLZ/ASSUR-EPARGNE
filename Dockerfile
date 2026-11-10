# Étape 1 : construire le front
FROM node:22-alpine AS front
WORKDIR /front
COPY ASSUR_EPARGNE_FRONT/package*.json ./
RUN npm ci
COPY ASSUR_EPARGNE_FRONT/ ./
RUN npm run build

# Étape 2 : construire le back, avec le front à l'intérieur
FROM eclipse-temurin:21-jdk AS back
WORKDIR /back
COPY ASSUR_EPARGNE_BACK/ ./
COPY --from=front /front/dist ./src/main/resources/static
RUN sh mvnw -q -DskipTests package

# Étape 3 : l'image finale, seulement Java et le .jar
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=back /back/target/*.jar app.jar
EXPOSE 8090
CMD ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
