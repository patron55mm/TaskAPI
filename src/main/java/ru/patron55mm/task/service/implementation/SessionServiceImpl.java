package ru.patron55mm.task.service.implementation;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.patron55mm.task.entity.User;
import ru.patron55mm.task.repository.UserRepository;
import ru.patron55mm.task.service.SessionService;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "users", key = "#email")
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    @Override
    @Cacheable(value = "users", key = "#email")
    public User getByEmailOrElseThrow(String email) {
        return getByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с email %s не найден".formatted(email)));
    }

    @Override
    public Optional<User> getAuthorizedUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof String email)
            return getByEmail(email);

        return Optional.empty();
    }

    @Override
    public User getAuthorizedUserOrElseThrow() {
        return getAuthorizedUser()
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }
}