package ru.example.galleryservice;

import lombok.SneakyThrows;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class TestWebFlux {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> elements = new ArrayList<>();

        Flux.just(1, 2, 3, 4)
                .log()
                .subscribeOn(Schedulers.single())
                .subscribe(new Subscriber<Integer>() {
                    private Subscription s;
                    int onNextAmount;

                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.s = subscription;
                        s.request(2);
                    }

                    @SneakyThrows
                    @Override
                    public void onNext(Integer integer) {
                        elements.add(integer);
                        onNextAmount++;
                        if (onNextAmount % 2 == 0) {
                            s.request(2);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Flux.just("1", "2", "3")
                .subscribe(value -> System.out.println("Value: " + value));

        Flux.just(1, 2, 3, 4, 5)
                .subscribe(value -> {
                    if (value > 4) {
                        throw new IllegalArgumentException(value + " > than 4");
                    }
                    System.out.println("Value: " + value);
                }, error -> System.out.println("Error: " + error.getMessage()));

        Flux.just(1, 2, 3, 4)
                .subscribe(value -> System.out.println("Value: " + value),
                        error -> {},
                        () -> System.out.println("Successfull"));

        Flux<Integer> ints = Flux.range(1, 5);
        Disposable done = ints.log().subscribe(System.out::println,
                error -> System.err.println("Error " + error),
                () -> System.out.println("Done"),
                sub -> sub.request(2));
        done.dispose();

        Mono<List<Integer>> listMono = Flux.just(1, 2, 3, 4)
                .filter(value -> value % 2 == 0)
                .collectList();
        System.out.println(listMono.block());

        Disposable disposable = Flux.just(1, 2, 3, 4, 5, 6, 7, 8)
                .delayElements(Duration.ofSeconds(3))
                .subscribe(value -> System.out.println("Value: " + value));

        Thread.sleep(7000);
        disposable.dispose();
        System.out.println("Cancelling subscription");
    }
}
