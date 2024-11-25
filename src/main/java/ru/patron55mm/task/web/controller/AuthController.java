package ru.patron55mm.task.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.patron55mm.task.service.AuthService;
import ru.patron55mm.task.web.WebConstant;
import ru.patron55mm.task.web.request.AuthenticationRequest;
import ru.patron55mm.task.web.response.AuthenticationResponse;
import ru.patron55mm.task.web.response.PaginatedResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "Контроллер авторизаций.")
@RequestMapping(WebConstant.VERSION_URL + "auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    @Operation(summary = "Авторизация пользователя.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PaginatedResponse.class)))
            })
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }
}