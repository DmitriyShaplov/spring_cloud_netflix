package ru.example.userservice.controller;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/")
public class MovieController {

    private static final Logger LOG = Logger.getLogger(UserController.class.getName());

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private Environment env;

    @GetMapping("/")
    public String home() {
        String home = "User-Service running at port: " + env.getProperty("local.server.port");
        LOG.log(Level.INFO, home);
        return home;
    }

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