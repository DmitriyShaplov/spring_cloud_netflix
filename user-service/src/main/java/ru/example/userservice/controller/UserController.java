package ru.example.userservice.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.example.userservice.client.ServiceFeignClient;
import ru.example.userservice.client.WebClientService;
import ru.example.userservice.model.Bucket;
import ru.example.userservice.client.TestRestTemplateService;

import java.util.List;

/**
 * РЕСТ контроллер для демонстрации возможностей вызова других сервисов
 * через: cloud FeignClient, RestTemplate, WebClient.
 */
@RestController
@Slf4j
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(MovieController.class.getName());

    @Autowired
    private ServiceFeignClient serviceFeignClient;
    @Autowired
    private TestRestTemplateService restTemplateService;
    @Autowired
    private WebClientService webClientService;
    @Autowired
    private Environment env;

    @GetMapping("/")
    public String home() {
        String home = "User-Service running at port: " + env.getProperty("local.server.port");
        logger.info(home);
        return home;
    }

    /**
     * Получение данных с помощью FeignClient'a от spring.cloud.openfeign
     */
    @GetMapping("/getAllDataFromGalleryService")
    public List<Bucket> getDataByFeignClient() {
        logger.info("Calling through Feign Client");
        return serviceFeignClient.getAllEmployeeList();
    }

    /**
     * Получение данных с помощью RestTemplate
     */
    @GetMapping("/data")
    public String data() {
        logger.info("Calling through RestTemplate");
        return restTemplateService.data();
    }

    /**
     * Получение реактивных данных с помощью WebClient
     */
    @GetMapping(value = "/getDataByWebClient", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Bucket> getDataByWebClient() {
        logger.info("Calling through WebClient");
        return webClientService.getDataByWebClient();
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<MyCustomServerException> handleWebClientResponseException(DataAccessException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MyCustomServerException("A Bucket with the same title already exists"));
    }

    @Data
    public static class MyCustomServerException {
        private final String response;

        public MyCustomServerException(String response) {
            this.response = response;
        }
    }
}
