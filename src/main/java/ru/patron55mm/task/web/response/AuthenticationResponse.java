package ru.patron55mm.task.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ с токеном доступа.")
public record AuthenticationResponse(
        @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzM4NCJ9.eyJsb2d...") String token
) {

}