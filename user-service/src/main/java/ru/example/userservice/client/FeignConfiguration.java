package ru.example.userservice.client;

import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class FeignConfiguration {

    @Bean
    public RequestInterceptor logRequestInterceptor() {
        return request -> {
            request.header("TEST", "test");
            log.info("Добавил интерцептор к запросу");
        };
    }
}
