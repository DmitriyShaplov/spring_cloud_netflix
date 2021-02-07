package ru.example.galleryservice.service;

import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.example.galleryservice.exception.BucketNotFoundException;
import ru.example.galleryservice.model.Bucket;
import ru.example.galleryservice.payload.ErrorResponse;
import ru.example.galleryservice.repository.BucketRepository;

import javax.validation.Valid;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class BucketService {

    private final BucketRepository bucketRepository;

    @Transactional(readOnly = true)
    public Flux<Bucket> getAllEmployeesList() {
        return bucketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Flux<Bucket> data() {
        return bucketRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Flux<Bucket> getAllBuckets() {
        return bucketRepository.findAll();
    }

    @Transactional
    public Mono<Bucket> createBucket(@Valid @RequestBody Bucket bucket) {
        return bucketRepository.save(bucket);
    }

    @Transactional(readOnly = true)
    public Mono<ResponseEntity<Bucket>> getBucketById(String bucketId) {
        return bucketRepository.findById(bucketId)
                .map(ResponseEntity::ok)  // then the map operator is called on this Bucket to wrap it in a ResponseEntity object with status code 200 OK
                .defaultIfEmpty(ResponseEntity.notFound().build()); // finally there is a call to defaultIfEmpty to build an empty ResponseEntity with status 404 NOT FOUND if the Bucket was not found.
    }

    @Transactional
    public Mono<ResponseEntity<Bucket>> updateBucket(String bucketId,
                                                     Bucket bucket) {
        return bucketRepository.findById(bucketId)
                .flatMap(existingBucket -> {
                    existingBucket.setDescription(bucket.getDescription());
                    existingBucket.setImageLink(bucket.getImageLink()); // then calls flatMap with this movie to update its entries using its setters and the values from the Bucket passed as argument.
                    return bucketRepository.save(existingBucket);
                })
                .map(ResponseEntity::ok) // Then it saves them to the database and wraps this updated Bucket in a ResponseEntity with status code 200 OK in case of success or 404 NOT FOUND in case of failure.
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Transactional
    public Mono<ResponseEntity<Void>> deleteBucket(String bucketId) {
        return bucketRepository.findById(bucketId) // First, you search the Bucket you want to delete.
                .flatMap(existingBucket ->
                        bucketRepository.delete(existingBucket)
                                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());  // or you return 404 NOT FOUND to say the Bucket was not found
    }

    @Transactional
    public Mono<Void> deleteAllBuckets() {
        return bucketRepository.deleteAll();
    }

    public Flux<Bucket> streamAllBuckets() {
        return bucketRepository.findAll();
    }

    public Flux<Bucket> emitBuckets() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(val -> new Bucket("" + val, "Docker",
                        "default theme", 0, "docker-site"));
    }

    public Flux<Bucket> streamAllBucketsDelay() {
        return bucketRepository.findAll().delayElements(Duration.ofSeconds(2));
    }
}
