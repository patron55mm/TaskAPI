package ru.patron55mm.task.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.patron55mm.task.entity.User;
import ru.patron55mm.task.service.SessionService;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SessionService service;

    @Override
    @Cacheable(value = "userDetails", key = "#email")
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = service.getByEmailOrElseThrow(email);

        return new CustomUserDetails(user);
    }
}