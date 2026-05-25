package com.outsera.goldenraspberry.application.usecase;

import com.outsera.goldenraspberry.application.port.in.AwardIntervalResult;
import com.outsera.goldenraspberry.domain.model.Movie;
import com.outsera.goldenraspberry.domain.model.ProducerInterval;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ProducerIntervalCalculator {

    public AwardIntervalResult calculate(List<Movie> winners) {
        var winsByProducer = winners.stream()
                .flatMap(movie -> movie.producerNames().stream()
                        .map(name -> Map.entry(name.value(), movie.year())))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        var allIntervals = winsByProducer.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .flatMap(entry -> {
                    var sortedYears = entry.getValue().stream().sorted().toList();
                    return IntStream.range(1, sortedYears.size())
                            .mapToObj(i -> new ProducerInterval(
                                    entry.getKey(),
                                    sortedYears.get(i) - sortedYears.get(i - 1),
                                    sortedYears.get(i - 1),
                                    sortedYears.get(i)));
                })
                .toList();

        if (allIntervals.isEmpty()) {
            return new AwardIntervalResult(List.of(), List.of());
        }

        int minInterval = allIntervals.stream().mapToInt(ProducerInterval::interval).min().orElseThrow();
        int maxInterval = allIntervals.stream().mapToInt(ProducerInterval::interval).max().orElseThrow();

        return new AwardIntervalResult(
                allIntervals.stream().filter(p -> p.interval() == minInterval).toList(),
                allIntervals.stream().filter(p -> p.interval() == maxInterval).toList()
        );
    }
}
