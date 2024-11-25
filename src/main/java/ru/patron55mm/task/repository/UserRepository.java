package ru.patron55mm.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.patron55mm.task.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
}