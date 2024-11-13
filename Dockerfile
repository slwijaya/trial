# Gunakan image dasar JDK untuk menjalankan aplikasi
FROM eclipse-temurin:17-jdk-alpine

# Buat directory untuk aplikasi
WORKDIR /app

# Copy file build.gradle dan settings.gradle (untuk dependency caching)
COPY build.gradle settings.gradle /app/

# Copy file source code ke container
COPY src /app/src

# Copy file gradlew dan wrapper
COPY gradlew /app/
COPY gradle /app/gradle

# Run gradle build untuk mengunduh dependencies dan build aplikasi
RUN ./gradlew build --no-daemon

# Jalankan jar file yang sudah dihasilkan
CMD ["java", "-jar", "/app/build/libs/<NAMA_APLIKASI>.jar"]
