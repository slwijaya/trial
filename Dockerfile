# VM
# Gunakan image dasar JDK untuk menjalankan aplikasi
# FROM eclipse-temurin:17-jdk-alpine

# Buat directory untuk aplikasi
# WORKDIR /app

# Copy file build.gradle dan settings.gradle (untuk dependency caching)
# COPY build.gradle settings.gradle /app/

# Copy file source code ke container
# COPY src /app/src

# Copy file gradlew dan wrapper
# COPY gradlew /app/
# COPY gradle /app/gradle

# Run gradle build untuk mengunduh dependencies dan build aplikasi
# RUN ./gradlew build --no-daemon

# Jalankan jar file yang sudah dihasilkan
# CMD ["java", "-jar", "/app/build/libs/<NAMA_APLIKASI>.jar"]

# local
# Gunakan image JDK 17 untuk menjalankan Spring Boot
# FROM eclipse-temurin:17-jdk-alpine
# FROM openjdk:17-jdk-alpine
FROM openjdk:17-slim

# Instal xargs dan alat-alat dasar lainnya
RUN apt-get update && apt-get install -y bash coreutils

# Buat directory untuk aplikasi
WORKDIR /app

# Copy build.gradle dan settings.gradle untuk caching dependencies
COPY build.gradle settings.gradle /app/

# Copy source code
COPY src /app/src

# Copy file gradlew dan wrapper
COPY gradlew /app/
COPY gradle /app/gradle

# Build aplikasi tanpa menjalankan tes
RUN ./gradlew build -x test --no-daemon

# Jalankan jar file yang dihasilkan
# CMD ["java", "-jar", "/app/build/libs/productapi.jar"]
CMD ["java", "-jar", "/app/build/libs/productApi-0.0.1-SNAPSHOT.jar"]