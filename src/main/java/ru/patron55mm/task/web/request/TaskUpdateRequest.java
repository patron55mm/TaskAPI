package ru.patron55mm.task.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
@Schema(description = "Запрос на изменение задачи.")
public class TaskUpdateRequest {

    @Schema(description = "Название", example = "Test Task")
    @Size(max = 300, message = "Название не может превышать 300 символов.")
    private String title;

    @Schema(description = "Описание", example = "example@example.ru")
    @Size(max = 1000, message = "Название не может превышать 300 символов.")
    private String description;
}