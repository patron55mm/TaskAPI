package ru.patron55mm.task.repository.task;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.patron55mm.task.entity.task.Task;
import ru.patron55mm.task.entity.task.TaskComment;

import java.util.List;

public interface TaskCommentRepository extends JpaRepository<TaskComment, Long> {

    List<TaskComment> findAllByTask(Task task);
}