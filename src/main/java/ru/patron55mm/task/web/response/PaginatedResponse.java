package ru.patron55mm.task.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Ответ с пагинацией.")
public record PaginatedResponse<T>(
        @Schema(description = "Содержимое страницы") List<T> content,
        @Schema(description = "Текущая страница", example = "0") int currentPage,
        @Schema(description = "Всего страниц", example = "1") int totalPages,
        @Schema(description = "Всего элементов", example = "50") long totalItems) {

}