package ru.example.authservice.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Класс с конфигурационными настройками для JWT
 */
@Component
@ConfigurationProperties(prefix = "security.jwt")
@Data
public class JwtConfig {
    private String uri = "/auth/**";
    private String header = "Authorization";
    private String prefix = "Bearer ";
    private int expiration = 24 * 60 * 60;
    private String secret = "JwtSecretKey";
}
