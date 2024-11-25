package ru.patron55mm.task.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.patron55mm.task.exc.WrongCredentialsException;
import ru.patron55mm.task.service.AuthService;
import ru.patron55mm.task.service.JwtService;
import ru.patron55mm.task.web.request.AuthenticationRequest;
import ru.patron55mm.task.web.response.AuthenticationResponse;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            ));

            if (authenticate.isAuthenticated()) {
                log.info("User {} auth", request.getEmail());

                return new AuthenticationResponse(jwtService.generateToken(request.getEmail()));
            }

            throw new WrongCredentialsException("Неверные данные");
        } catch (AuthenticationException e) {
            throw new WrongCredentialsException("Неверные данные");
        }
    }
}