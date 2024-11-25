package ru.patron55mm.task.utils.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleEnum {
    ADMIN("Администратор"),
    USER("Пользователь");

    private final String textRole;
}