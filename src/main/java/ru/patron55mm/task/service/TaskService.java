package ru.patron55mm.task.service;

import ru.patron55mm.task.utils.enums.PriorityEnum;
import ru.patron55mm.task.utils.enums.StatusEnum;
import ru.patron55mm.task.web.request.TaskCreateRequest;
import ru.patron55mm.task.web.request.TaskUpdateRequest;
import ru.patron55mm.task.web.response.PaginatedResponse;
import ru.patron55mm.task.web.response.Response;
import ru.patron55mm.task.web.response.TaskWithCommentsResponse;

public interface TaskService {

    PaginatedResponse<TaskWithCommentsResponse> getTasks(String title, String executor, String author, PriorityEnum priority, StatusEnum status, Integer page);

    Response createTask(TaskCreateRequest request);

    Response updateTask(Long id, TaskUpdateRequest request);

    Response deleteTask(Long id);

    Response commentTask(Long id, String comment);

    Response changeStatusTask(Long id, StatusEnum status);

    Response changeExecutorTask(Long id, String email);

    Response changePriorityTask(Long id, PriorityEnum priority);
}