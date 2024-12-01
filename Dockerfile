# Этап сборки
FROM maven:3.9.4-amazoncorretto-21 AS builder

# Устанавливаем рабочую директорию для сборки
WORKDIR /app

# Копируем только pom.xml и загружаем зависимости, чтобы использовать кэш Docker для повторных сборок
COPY pom.xml ./
RUN mvn dependency:go-offline -B

# Копируем исходный код приложения
COPY src ./src

# Вставка .env в контейнер
COPY .env /app/.env

# Собираем приложение, пропуская тесты
RUN mvn clean package -DskipTests

# Этап выполнения (производственный контейнер)
FROM openjdk:21-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем только собранный .jar файл
COPY --from=builder /app/target/*.jar app.jar

# Экспонируем порт 8080
EXPOSE 8080

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
