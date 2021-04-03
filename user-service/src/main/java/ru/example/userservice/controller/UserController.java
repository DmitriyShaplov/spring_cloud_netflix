package ru.example.userservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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

    @Autowired
    private ServiceFeignClient serviceFeignClient;
    @Autowired
    private TestRestTemplateService restTemplateService;
    @Autowired
    private WebClientService webClientService;

    /**
     * Получение данных с помощью FeignClient'a от spring.cloud.openfeign
     */
    @GetMapping("/getAllDataFromGalleryService")
    public List<Bucket> getDataByFeignClient() {
        return serviceFeignClient.getAllEmployeeList();
    }

    /**
     * Получение данных с помощью RestTemplate
     */
    @GetMapping("/data")
    public String data() {
        return restTemplateService.data();
    }

    /**
     * Получение реактивных данных с помощью WebClient
     */
    @GetMapping(value = "/getDataByWebClient", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Bucket> getDataByWebClient() {
        return webClientService.getDataByWebClient();
    }
}
