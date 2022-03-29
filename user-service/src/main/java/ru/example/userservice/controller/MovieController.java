package ru.example.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/")
public class MovieController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/getAllMovies")
    public String getAllMovies() {
        String result = restTemplate.getForObject("http://movie-service/getAll", String.class);
        return result;
    }

    @RequestMapping("/get")
    public String getInfoFromMovieService() {
        String result = this.restTemplate.getForObject("http://movie-service/", String.class);
        return result;
    }

}