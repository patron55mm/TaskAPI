# Используем официальный образ Gradle с JDK 21 для сборки
FROM gradle:8.11.1-jdk21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем все необходимые файлы для сборки
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle gradle
COPY src src

# Выполняем сборку проекта
RUN ./gradlew clean build --no-daemon

# Используем JDK образ для запуска приложения
FROM eclipse-temurin:21-jdk-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем готовый JAR-файл из предыдущего этапа
COPY --from=builder /app/build/libs/*.jar app.jar

# Пробрасываем порт приложения
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-Dspring.profiles.active=dev", "-jar", "app.jar"]