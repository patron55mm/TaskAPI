package ru.patron55mm.task.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Базовый ответ.")
public record Response(
        @Schema(description = "Сообщение с ответом") String message
) {

}