package ru.patron55mm.task.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import ru.patron55mm.task.utils.enums.PriorityEnum;

@Getter
@Schema(description = "Запрос на создание задачи.")
public class TaskCreateRequest {

    @Schema(description = "Название", example = "Test Task")
    @NotBlank(message = "Название не может быть пустым.")
    @Size(max = 300, message = "Название не может превышать 300 символов.")
    private String title;

    @Schema(description = "Описание", example = "example@example.ru")
    @Size(max = 1000, message = "Описание не может превышать 1000 символов.")
    private String description;

    @Schema(description = "Приоритет", example = "HIGH")
    @NotNull(message = "Приоритет обязателен.")
    private PriorityEnum priority;

    @Schema(description = "Исполнитель", example = "executor@example.ru")
    @NotBlank(message = "Email исполнителя не может быть пустым.")
    @Email(message = "Некорректный формат email.")
    private String executor;
}