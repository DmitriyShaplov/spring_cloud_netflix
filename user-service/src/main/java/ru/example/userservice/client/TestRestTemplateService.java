package ru.example.userservice.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class TestRestTemplateService {

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "failed")
    public String data() {
        String response = restTemplate.getForObject("http://gallery-service/data", String.class);
        log.info(response);
        return response;
    }

    public String failed() {
        String error = "Service is not available now. Please try later";
        log.info(error);
        return error;
    }
}
