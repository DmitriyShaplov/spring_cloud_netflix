package ru.example.galleryservice.controller;

import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.example.galleryservice.exception.BucketNotFoundException;
import ru.example.galleryservice.model.Bucket;
import ru.example.galleryservice.payload.ErrorResponse;
import ru.example.galleryservice.service.BucketService;

@RestController
@RequestMapping("/")
@Slf4j
@RequiredArgsConstructor
public class BucketController {

    private final Environment env;

    private final BucketService bucketService;

    @RequestMapping("/")
    public String home() {
        String home = "Gallery-Service running at port: " + env.getProperty("local.server.port");
        log.info(home);
        return home;
    }

    @GetMapping("/show")
    public Flux<Bucket> getAllEmployeesList() {
        log.info("Get data from database (Feign Client on User-Service side)");
        return bucketService.getAllEmployeesList();
    }

    @GetMapping("/data")
    public Flux<Bucket> data() {
        log.info("Get data from database (RestTemplate on User-Service side)");
        return bucketService.data();
    }

    @GetMapping("/getAll")
    public Flux<Bucket> getAllBuckets() {
        return bucketService.getAllBuckets();
    }

    @PostMapping("/create")
    public Mono<Bucket> createBucket(Bucket bucket) {
        return bucketService.createBucket(bucket);
    }

    @GetMapping("/get/{id}")
    public Mono<ResponseEntity<Bucket>> getBucketById(String bucketId) {
        return bucketService.getBucketById(bucketId);
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<Bucket>> updateBucket(String bucketId, Bucket bucket) {
        return bucketService.updateBucket(bucketId, bucket);
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> deleteBucket(String bucketId) {
        return bucketService.deleteBucket(bucketId);
    }

    @DeleteMapping("/deleteAllBuckets")
    public Mono<Void> deleteAllBuckets() {
        return bucketService.deleteAllBuckets();
    }

    @GetMapping(value = "/stream/buckets", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Bucket> streamAllBuckets() {
        return bucketService.streamAllBuckets();
    }

    @GetMapping(value = "/stream/buckets/default", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Bucket> emitBuckets() {
        return bucketService.emitBuckets();
    }

    @GetMapping(value = "/stream/buckets/delay", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Bucket> streamAllBucketsDelay() {
        log.info("Get data from database (WebClient on User-Service side)");
        return bucketService.streamAllBucketsDelay();
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateKeyException(DuplicateKeyException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponse("A Bucket with the same title already exists")
        );
    }

    @ExceptionHandler(BucketNotFoundException.class)
    public ResponseEntity<Void> handleBucketNotFoundException(BucketNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }
}
