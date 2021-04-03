package ru.example.galleryservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.example.galleryservice.model.Bucket;

/**
 * Реактивный репозиторий для тем.
 */
@Repository
public interface BucketRepository extends ReactiveMongoRepository<Bucket, String> {
}
