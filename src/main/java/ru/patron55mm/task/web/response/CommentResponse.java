package ru.patron55mm.task.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Ответ с комментарием.")
public record CommentResponse(
        @Schema(description = "ID комментария", example = "1") Long id,
        @Schema(description = "Комментарий", example = "Comment") String comment,
        @Schema(description = "Почта комментатора", example = "author@example.ru") String userEmail,
        @Schema(description = "Дата создания комментария", example = "2024-11-25T9:00:00.000000") LocalDateTime created) {

}