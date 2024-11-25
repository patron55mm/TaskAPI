package ru.patron55mm.task.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.patron55mm.task.entity.User;
import ru.patron55mm.task.entity.task.Task;
import ru.patron55mm.task.entity.task.TaskComment;
import ru.patron55mm.task.repository.task.TaskCommentRepository;
import ru.patron55mm.task.repository.task.TaskRepository;
import ru.patron55mm.task.service.SessionService;
import ru.patron55mm.task.service.TaskService;
import ru.patron55mm.task.utils.enums.PriorityEnum;
import ru.patron55mm.task.utils.enums.RoleEnum;
import ru.patron55mm.task.utils.enums.StatusEnum;
import ru.patron55mm.task.web.request.TaskCreateRequest;
import ru.patron55mm.task.web.request.TaskUpdateRequest;
import ru.patron55mm.task.web.response.PaginatedResponse;
import ru.patron55mm.task.web.response.Response;
import ru.patron55mm.task.web.response.TaskWithCommentsResponse;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

    private static final int DEFAULT_PAGE_SIZE = 50;
    private final TaskRepository taskRepository;
    private final TaskCommentRepository commentRepository;
    private final SessionService sessionService;
    private final ModelMapper modelMapper;

    @Override
    public PaginatedResponse<TaskWithCommentsResponse> getTasks(String title, String executorEmail, String authorEmail, PriorityEnum priority, StatusEnum status, Integer page) {
        User user = getAuthUser();
        log.info("Receiving tasks from {}", user.getEmail());
        User executor = null;

        if (StringUtils.hasText(executorEmail))
            executor = sessionService.getByEmailOrElseThrow(executorEmail);
        else if (user.getRole() != RoleEnum.ADMIN)
            executor = user;

        Page<Task> taskPage = taskRepository.findAllBy(
                title,
                executor,
                authorEmail,
                priority,
                status,
                PageRequest.of(page, DEFAULT_PAGE_SIZE)
        );

        List<TaskWithCommentsResponse> tasks = getTasksWithComments(taskPage);

        return new PaginatedResponse<>(tasks, taskPage.getNumber(), taskPage.getTotalPages(), taskPage.getTotalElements());
    }

    @Override
    public Response createTask(TaskCreateRequest request) {
        log.info("Creating task with title: {}", request.getTitle());
        saveTask(Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .executor(sessionService.getByEmailOrElseThrow(request.getExecutor()))
                .author(getAuthUser())
                .priority(request.getPriority())
                .status(StatusEnum.WAIT)
                .build());

        return new Response("Задача %s создана.".formatted(request.getTitle()));
    }

    @Override
    public Response updateTask(Long id, TaskUpdateRequest request) {
        log.info("Update task with title: {}", request.getTitle());
        Task task = getTaskByID(id);

        if (StringUtils.hasText(request.getTitle()))
            task.setTitle(request.getTitle());

        if (StringUtils.hasText(request.getDescription()))
            task.setDescription(request.getDescription());

        saveTask(task);

        return new Response("Задача %s обновлена.".formatted(task.getTitle()));
    }

    @Override
    public Response deleteTask(Long id) {
        log.info("Delete task with id: {}", id);
        taskRepository.deleteById(id);

        return new Response("Задача %s удалена.".formatted(id));
    }

    @Override
    public Response commentTask(Long id, String comment) {
        log.info("Comment task with id: {}", id);
        Task task = getTaskByID(id);
        User user = getAuthUser();

        TaskComment taskComment = TaskComment.builder()
                .task(task)
                .user(user)
                .comment(comment)
                .build();

        commentRepository.save(taskComment);

        return new Response("Вы оставили комментарий на задаче %s.".formatted(task.getTitle()));
    }

    @Override
    public Response changeStatusTask(Long id, StatusEnum status) {
        log.info("Change status task with id: {}", id);
        Task task = getTaskByID(id);
        task.setStatus(status);
        saveTask(task);

        return new Response("Статус задачи %s изменен.".formatted(task.getTitle()));
    }

    @Override
    public Response changeExecutorTask(Long id, String executorEmail) {
        log.info("Change executor task with id: {}", id);
        Task task = getTaskByID(id);
        User user = sessionService.getByEmailOrElseThrow(executorEmail);
        task.setExecutor(user);
        saveTask(task);

        return new Response("Исполнитель задачи %s изменен.".formatted(task.getTitle()));
    }

    @Override
    public Response changePriorityTask(Long id, PriorityEnum priority) {
        log.info("Change priority task with id: {}", id);
        Task task = getTaskByID(id);
        task.setPriority(priority);
        saveTask(task);

        return new Response("Приоритет задачи %s изменен.".formatted(task.getTitle()));
    }

    /**
     * Является ли текущий пользователь исполнителем задачи
     *
     * @param id ID задачи
     */
    public boolean isExecutor(Long id) {
        User user = getAuthUser();
        Task task = getTaskByID(id);

        return task.getExecutor().equals(user);
    }

    /**
     * Получение {@link TaskWithCommentsResponse} ответа задач с комментариями
     *
     * @param taskPage {@link Page<Task>} задачи с пагинацией
     * @return {@link List<TaskWithCommentsResponse>} список с задачами
     */
    private List<TaskWithCommentsResponse> getTasksWithComments(Page<Task> taskPage) {
        return taskPage.stream()
                .map(task -> modelMapper.map(task, TaskWithCommentsResponse.class))
                .toList();
    }

    /**
     * Получение задачи по ID
     *
     * @param id ID задачи
     * @return {@link Task} объект задачи
     */
    private Task getTaskByID(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Задача с ID %s не найдена".formatted(id)));
    }

    /**
     * Сохранение задачи в репозиторий
     *
     * @param task объект задачи
     */
    private void saveTask(Task task) {
        taskRepository.save(task);
    }

    /**
     * Получение авторизованного пользователя
     *
     * @return {@link User} объект авторизованного пользователя
     */
    private User getAuthUser() {
        return sessionService.getAuthorizedUserOrElseThrow();
    }
}