package ru.example.movieservice.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import ru.example.movieservice.model.Movie;

@Repository
public interface MovieRepository extends ReactiveMongoRepository<Movie, String> {
}
