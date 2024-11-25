package ru.patron55mm.task.service;

public interface JwtService {

    /**
     * Извлечение имени пользователя из токена
     *
     * @param token токен
     * @return имя пользователя
     */
    String extractIssuer(String token);

    /**
     * Извлечение почты из токена
     *
     * @param token токен
     * @return имя пользователя
     */
    String extractEmail(String token);

    /**
     * Генерация токена
     *
     * @param email почта пользователя
     * @return токен
     */
    String generateToken(String email);
}