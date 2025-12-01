# Image de base avec Java 17
FROM eclipse-temurin:17-jdk-jammy

# Dossier de travail dans le conteneur
WORKDIR /app

# Copier le jar généré par Maven dans l'image
COPY target/student-management-0.0.1-SNAPSHOT.jar app.jar

# Port utilisé par l'application (souvent 8080 pour Spring Boot)
EXPOSE 8080

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
