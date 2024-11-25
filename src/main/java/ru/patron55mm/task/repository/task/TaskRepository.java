package ru.patron55mm.task.repository.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.patron55mm.task.entity.User;
import ru.patron55mm.task.entity.task.Task;
import ru.patron55mm.task.utils.enums.PriorityEnum;
import ru.patron55mm.task.utils.enums.StatusEnum;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM tasks t " +
            "JOIN FETCH t.executor " +
            "JOIN FETCH t.author " +
            "WHERE (:executor IS NULL OR t.executor = :executor) " +
            "AND (:authorEmail IS NULL OR t.author.email = :authorEmail) " +
            "AND (:title IS NULL OR t.title LIKE %:title%) " +
            "AND (:priority IS NULL OR t.priority = :priority) " +
            "AND (:status IS NULL OR t.status = :status)")
    Page<Task> findAllBy(@Param("title") String title,
                         @Param("executor") User executor,
                         @Param("authorEmail") String authorEmail,
                         @Param("priority") PriorityEnum priority,
                         @Param("status") StatusEnum status,
                         Pageable pageable);
}