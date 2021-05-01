package ru.example.userservice.client;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.example.userservice.model.Bucket;

@Service
public class WebClientService {

    private final WebClient webClient;

    public WebClientService() {
//        WebClient webClient = WebClient.create("https://localhost:8654/getData");
        this.webClient = WebClient.builder()
                .baseUrl("http://localhost:8081")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.USER_AGENT, "User service")
//                .filter(ExchangeFilterFunctions.basicAuthentication())
                .build();
    }

    public Flux<Bucket> getDataByWebClient() {
        //                .bodyValue(new Object())
        //                .body(BodyInserters.fromValue(new Object()))
        //                .body(BodyInserters.fromPublisher(Mono.just("data"), String.class))
        return webClient
                .get()
                .uri("/stream/buckets/delay")
                .exchange()
                .flatMapMany(clientResponse -> clientResponse.bodyToFlux(Bucket.class));

        //пример обработки ошибок:
//        webClient.get().uri("user/repos?sort={sortField}&direction={sortDirection}",
//                "updated", "desc")
//                .retrieve()
//                .onStatus(HttpStatus::is4xxClientError, clientResponse ->
//                        Mono.error(new MyCustomClientException())
//                )
//                .onStatus(HttpStatus::is5xxServerError, clientResponse ->
//                        Mono.error(new MyCustomServerException())
//                )
//                .bodyToFlux(MyRepo.class);

    }
}
