package ru.example.userservice.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.example.userservice.model.Bucket;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class Fallback implements ServiceFeignClient {
    @Override
    public List<Bucket> getAllEmployeeList() {
        log.info("ERROR: Service is not available now");
        return Collections.emptyList();
    }
}
