package ru.example.galleryservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * Модель с темами для изучения.
 */
@Data
@Builder
@AllArgsConstructor
@Document(collection = "buckets")
public class Bucket {

    @Id
    private String id;

    @NotBlank
    @Size(max = 10)
    private String title;

    private String description;
    private int personalNumber;
    private String imageLink;
}
