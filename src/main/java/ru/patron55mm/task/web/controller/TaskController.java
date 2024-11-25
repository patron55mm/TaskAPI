package ru.patron55mm.task.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.patron55mm.task.service.TaskService;
import ru.patron55mm.task.utils.enums.PriorityEnum;
import ru.patron55mm.task.utils.enums.StatusEnum;
import ru.patron55mm.task.web.WebConstant;
import ru.patron55mm.task.web.request.TaskCreateRequest;
import ru.patron55mm.task.web.request.TaskUpdateRequest;
import ru.patron55mm.task.web.response.PaginatedResponse;
import ru.patron55mm.task.web.response.Response;
import ru.patron55mm.task.web.response.TaskWithCommentsResponse;

@Validated
@RestController
@AllArgsConstructor
@Tag(name = "Контроллер заданий.")
@RequestMapping(WebConstant.VERSION_URL + "task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    @Operation(summary = "Получение всех задач.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<PaginatedResponse<TaskWithCommentsResponse>> getTasks(@RequestParam(required = false) String title,
                                                                                @RequestParam(required = false) String executor,
                                                                                @RequestParam(required = false) String author,
                                                                                @RequestParam(required = false) PriorityEnum priority,
                                                                                @RequestParam(required = false) StatusEnum status,
                                                                                @RequestParam(defaultValue = "0") Integer page) {
        return ResponseEntity.ok(taskService.getTasks(title, executor, author, priority, status, page));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Создание задачи.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<Response> createTask(@Valid @RequestBody TaskCreateRequest request) {
        return ResponseEntity.ok(taskService.createTask(request));
    }

    @PatchMapping("update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновление задачи.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<Response> updateTask(@PathVariable Long id,
                                               @Valid @RequestBody TaskUpdateRequest request) {
        return ResponseEntity.ok(taskService.updateTask(id, request));
    }

    @PutMapping("comment/{id}")
    @PreAuthorize("hasRole('ADMIN') or @taskServiceImpl.isExecutor(#id)")
    @Operation(summary = "Комментирование задачи.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<Response> commentTask(@PathVariable Long id,
                                                @Valid @RequestBody @NotBlank(message = "Комментарий не может быть пустым.") String comment) {
        return ResponseEntity.ok(taskService.commentTask(id, comment));
    }

    @PatchMapping("status/{id}")
    @PreAuthorize("hasRole('ADMIN') or @taskServiceImpl.isExecutor(#id)")
    @Operation(summary = "Изменение статуса задачи.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<Response> changeStatusTask(@PathVariable Long id,
                                                     @Valid @RequestBody @NotNull(message = "Статус не может быть пустым.") StatusEnum status) {
        return ResponseEntity.ok(taskService.changeStatusTask(id, status));
    }

    @PatchMapping("executor/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Изменение исполнителя задачи.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<Response> changeExecutorTask(@PathVariable Long id,
                                                       @Valid @RequestBody @NotBlank(message = "Email исполнителя не может быть пустым.") @Email(message = "Некорректный формат email.") String executorEmail) {
        return ResponseEntity.ok(taskService.changeExecutorTask(id, executorEmail));
    }

    @PatchMapping("priority/{id}")
    @PreAuthorize("hasRole('ADMIN') or @taskServiceImpl.isExecutor(#id)")
    @Operation(summary = "Изменение приоритета задачи.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<Response> changePriorityTask(@PathVariable Long id,
                                                       @Valid @RequestBody @NotNull(message = "Приоритет не может быть пустым.") PriorityEnum priority) {
        return ResponseEntity.ok(taskService.changePriorityTask(id, priority));
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удаление задачи.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<Response> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }
}