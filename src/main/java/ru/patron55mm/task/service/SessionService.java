package ru.patron55mm.task.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.patron55mm.task.entity.User;

import java.util.Optional;

public interface SessionService {

    /**
     * Получение пользователя по почте
     *
     * @return {@link Optional<User>}
     */
    Optional<User> getByEmail(String credentials);

    /**
     * Получение пользователя по почте с исключением {@link UsernameNotFoundException}
     *
     * @return {@link User}
     */
    User getByEmailOrElseThrow(String credentials);

    /**
     * Получение текущего пользователя
     *
     * @return {@link Optional<User>}
     */
    Optional<User> getAuthorizedUser();

    /**
     * Получение текущего пользователя с исключением {@link UsernameNotFoundException}
     *
     * @return {@link User}
     */
    User getAuthorizedUserOrElseThrow();
}