package ru.example.galleryservice;

import reactor.core.publisher.Flux;

public class TestWork {

    private int testNumbers(int value) {
        if (value > 4) {
            System.out.println("Test checked");
        }
        return value;
    }

    private Flux<Integer> checkOnErrorMethodRetry() {
        Flux.range(1, 5)
                .map(this::testNumbers)
                .repeat(2)
                .subscribe(value -> System.out.println("Value: " + value),
                        error -> System.out.println("Error: " + error.getMessage()));

        return null;

    }

    public static void main(String[] args) {
        TestWork testWork = new TestWork();
        System.out.println("1" + testWork.checkOnErrorMethodRetry());
    }

}