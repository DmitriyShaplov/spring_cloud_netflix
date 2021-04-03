package ru.example.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ru.example.userservice.model.Bucket;

import java.util.List;

@FeignClient(name = "gallery-service", url = "http://localhost:8081",
        fallback = Fallback.class)
public interface ServiceFeignClient {

    @GetMapping("/show")
    List<Bucket> getAllEmployeeList();
}
