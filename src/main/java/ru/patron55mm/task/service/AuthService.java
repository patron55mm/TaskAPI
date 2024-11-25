package ru.patron55mm.task.service;

import ru.patron55mm.task.web.request.AuthenticationRequest;
import ru.patron55mm.task.web.response.AuthenticationResponse;

public interface AuthService {

    AuthenticationResponse authenticate(AuthenticationRequest request);
}