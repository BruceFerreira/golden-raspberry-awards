package com.outsera.goldenraspberry.application.usecase;

import com.outsera.goldenraspberry.application.port.in.AwardIntervalResult;
import com.outsera.goldenraspberry.domain.model.Movie;
import com.outsera.goldenraspberry.domain.model.ProducerInterval;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class ProducerIntervalCalculator {

    public AwardIntervalResult calculate(List<Movie> winners) {
        Map<String, List<Integer>> winYearsByProducer = winners.stream()
                .flatMap(movie -> movie.producerNames().stream()
                        .map(name -> Map.entry(name.value(), movie.year())))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));

        NavigableMap<Integer, List<ProducerInterval>> intervalsByGap = winYearsByProducer.entrySet().stream()
                .filter(entry -> entry.getValue().size() >= 2)
                .flatMap(entry -> consecutiveIntervals(entry.getKey(), entry.getValue()))
                .collect(Collectors.groupingBy(
                        ProducerInterval::interval,
                        TreeMap::new,
                        Collectors.toList()));

        if (intervalsByGap.isEmpty()) {
            return new AwardIntervalResult(List.of(), List.of());
        }

        return new AwardIntervalResult(
                intervalsByGap.firstEntry().getValue(),
                intervalsByGap.lastEntry().getValue());
    }

    private Stream<ProducerInterval> consecutiveIntervals(String producer, List<Integer> winYears) {
        List<Integer> sortedYears = winYears.stream().sorted().toList();
        return IntStream.range(1, sortedYears.size())
                .mapToObj(i -> new ProducerInterval(
                        producer,
                        sortedYears.get(i) - sortedYears.get(i - 1),
                        sortedYears.get(i - 1),
                        sortedYears.get(i)));
    }
}
