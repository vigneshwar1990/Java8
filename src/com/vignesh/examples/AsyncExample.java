package com.vignesh.examples;


import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vigneshwar Raghuram
 */

public class AsyncExample {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        Infrastructure.setDefaultExecutor(executorService);

        AsyncExample example = new AsyncExample();
        LocalDateTime start = LocalDateTime.now();
        System.out.println("Time starts-" + start);
        // int count = example.iterativeExample();


        example.asyncExample().onItem()
                .transform(t -> example.printUni(t, start))
                .subscribe().with(item -> {

                    System.out.println(">>> " + item.map(String::toLowerCase).await().indefinitely());
                });
        executorService.shutdown();

    }

    private Uni<String> printUni(Integer count, LocalDateTime start) {


        return Uni.createFrom().item(() -> {


            System.out.println("Count--" + count);

            LocalDateTime end = LocalDateTime.now();
            System.out.println("Time Ends-" + end);
            long diff = ChronoUnit.SECONDS.between(start, end);
            System.out.println(Thread.currentThread().getName() + "Time Difference-" + diff);
            return "Completed";
        });
    }

    private Uni<Integer> asyncExample() {

        List<Uni<Integer>> uniList = new ArrayList<>();

        Uni<Integer> uniCount1 = getUniCount(0, 250).runSubscriptionOn(Infrastructure.getDefaultExecutor());
        Uni<Integer> uniCount2 = getUniCount(250, 500).runSubscriptionOn(Infrastructure.getDefaultExecutor());
        Uni<Integer> uniCount3 = getUniCount(500, 1000).runSubscriptionOn(Infrastructure.getDefaultExecutor());

        uniList.add(uniCount1);
        uniList.add(uniCount2);
        uniList.add(uniCount3);


        return Uni.combine()
                .all().unis(uniList).combinedWith(
                        listOfResponses -> {
                            int sum =
                                    listOfResponses
                                            .stream()
                                            .mapToInt(listOfRespons -> (Integer) listOfRespons)
                                            .sum();

                            System.out.println("Total Sum Inside UNi" + sum);
                            return sum;
                        }
                );

    }

    private int iterativeExample() throws InterruptedException {
        int count = 0;

        for (int i = 0; i < 1000; ++i) {
            Thread.sleep(10L);
            ++count;
        }

        return count;
    }


    private int increaseCount(int numberStart, int numberEnd) throws InterruptedException {

        System.out.println(Thread.currentThread().getName() + "--Before Sleeping");
        Thread.sleep(9000);
        System.out.println(Thread.currentThread().getName() + "--After Sleep");
        return 250;

    }


    public Uni<Integer> getUniCount(int numberStart, int numberEnd) {
        return Uni.createFrom().item(() -> {
            try {
                return increaseCount(numberStart, numberEnd);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

}