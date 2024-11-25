package ru.patron55mm.task.utils;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.patron55mm.task.entity.User;
import ru.patron55mm.task.repository.UserRepository;
import ru.patron55mm.task.utils.enums.RoleEnum;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        if (repository.count() == 0) {
            String encodePassword = passwordEncoder.encode("password");

            User executor = User.builder()
                    .email("executor@example.ru")
                    .password(encodePassword)
                    .role(RoleEnum.USER)
                    .build();

            User author = User.builder()
                    .email("author@example.ru")
                    .password(encodePassword)
                    .role(RoleEnum.ADMIN)
                    .build();

            repository.saveAll(List.of(executor, author));
        }
    }
}