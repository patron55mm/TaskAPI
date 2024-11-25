package ru.patron55mm.task.service.implementation;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.patron55mm.task.service.JwtService;
import ru.patron55mm.task.utils.CustomUserDetailsService;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final CustomUserDetailsService customUserDetailsService;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    @Override
    public String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public String generateToken(String email) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        Map<String, Object> claims = new HashMap<>();

        return createToken(claims, userDetails);
    }

    /**
     * Извлечение данных из токена
     *
     * @param token           токен
     * @param claimsResolvers функция извлечения данных
     * @param <T>             тип данных
     * @return {@link T} данные
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);

        return claimsResolvers.apply(claims);
    }

    /**
     * Генерация токена
     *
     * @param claims      дополнительные данные
     * @param userDetails данные пользователя
     * @return {@link String} токен
     */
    private String createToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuer(userDetails.getAuthorities().iterator().next().getAuthority())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(Date.from(LocalDateTime.now().plusHours(1)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(getSignInKey())
                .compact();
    }

    /**
     * Извлечение всех данных из токена
     *
     * @param token токен
     * @return {@link Claims} данные из токена
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Получение ключа для подписи токена
     *
     * @return {@link SecretKey} секретный ключ
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}