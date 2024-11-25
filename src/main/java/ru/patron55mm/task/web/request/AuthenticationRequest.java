package ru.patron55mm.task.web.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@AllArgsConstructor
@Schema(description = "Запрос на аутентификацию.")
public class AuthenticationRequest {

  @Schema(description = "Почта", example = "author@example.ru")
  @Email(message = "Некорректный формат email.")
  @NotBlank(message = "Почта не может быть пустой.")
  private String email;

  @Schema(description = "Пароль", example = "password")
  @Size(min = 8, max = 64, message = "Пароль должен содержать от 8 до 64 символов.")
  @NotBlank(message = "Пароль не может быть пустым.")
  private String password;
}