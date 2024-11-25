# Task Management System

## Описание

Система управления задачами. Поддерживает функционал создания, редактирования, удаления и 
просмотра задач с разграничением прав доступа.

## Функционал

- Создание задач (только администратор).
- Редактирование задач (только администратор).
- Удаление задач (только администратор).
- Фильтрация задач по автору, исполнителю и статусу.
- Аутентификация и авторизация через JWT.
- Роли:
    - **Администратор**: Полный доступ.
    - **Пользователь**: Управление только своими задачами.

## Запуск проекта

### 1. Предварительные требования

- **Java 21**
- **Docker** и **Docker Compose**

### 2. Настройка и запуск

1. Клонируйте репозиторий:
    ```bash
    git clone https://github.com/patron55mm/TaskAPI.git
    cd TaskAPI
    ```
2. Запустите сервисы через Docker Compose:
    ```bash
    docker-compose up --build
    ```
3. После успешного запуска:
    - API будет доступно по адресу: [http://localhost:8080](http://localhost:8080)
    - Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 3. Использование API

1. **Получить JWT токен:**
    - Для тестов в приложении создаются два пользователя: author@example.ru (права администратора) и executor@example.ru (права обычного пользователя)
    - Отправьте POST-запрос на `/v1/auth/login` с телом:
      ```json
      {
        "email": "author@example.ru",
        "password": "password"
      }
      ```
2. Используйте полученный токен для доступа к защищённым эндпоинтам.

### 4. Примеры эндпоинтов

- **Создать задачу (только админ):**
  ```http
  POST /v1/task
  Content-Type: application/json
  Authorization: Bearer <JWT>
  
  {
    "title": "Test Task",
    "description": "This is a test task",
    "priority": "HIGH",
    "executor": "executor@example.com"
  }