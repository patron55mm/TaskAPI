package ru.patron55mm.task.exc;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException exc,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exc.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "message", "Неверно указаны данные",
                        "errors", errors
                ));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException exc,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        return ResponseEntity.badRequest()
                .body("""
                            {
                                "error": "Некорректный формат данных"
                            }
                        """);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Map<String, String>> handleAllException(Exception exc) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", exc.getMessage());
        return ResponseEntity.badRequest()
                .body(errors);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, String>> usernameNotFoundException(UsernameNotFoundException exc) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errors);
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public ResponseEntity<Map<String, String>> wrongCredentialsException(WrongCredentialsException exc) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", exc.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDeniedException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body("""
                                {
                                    "error": "Доступ запрещен"
                                }
                        """);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> entityNotFoundException(EntityNotFoundException exc) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", exc.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException exc) {
        if (exc.getMessage().contains("Duplicate entry"))
            return ResponseEntity.badRequest()
                    .body("""
                                {
                                    "error": "Похожая запись уже существует"
                                }
                            """);

        return ResponseEntity.internalServerError()
                .body("""
                            {
                                "error": "Похожая запись уже существует"
                            }
                        """);
    }
}