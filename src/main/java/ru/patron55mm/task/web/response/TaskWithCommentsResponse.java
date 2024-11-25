package ru.patron55mm.task.web.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.patron55mm.task.utils.enums.PriorityEnum;
import ru.patron55mm.task.utils.enums.StatusEnum;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Ответ с задачей и комментариями.")
public record TaskWithCommentsResponse(
        @Schema(description = "ID задачи", example = "1") Long id,
        @Schema(description = "Название задачи", example = "Задача") String title,
        @Schema(description = "Описание задачи", example = "Описание..") String description,
        @Schema(description = "Приоритет задачи", example = "HIGH") PriorityEnum priority,
        @Schema(description = "Статус задачи", example = "WAIT") StatusEnum status,
        @Schema(description = "Почта исполнителя", example = "executor@example.ru") String executorEmail,
        @Schema(description = "Почта автора", example = "author@example.ru") String authorEmail,
        @Schema(description = "Список комментариев") List<CommentResponse> comments,
        @Schema(description = "Дата создания задачи", example = "2024-11-25T9:00:00.000000") LocalDateTime created
) {

}