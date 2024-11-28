# Usa una imagen base ligera de Java
FROM openjdk:17-jdk-slim

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR generado al contenedor
COPY target/proyecto-1.jar app.jar

# Exponer el puerto que usa la aplicación (generalmente definido en application.properties)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
