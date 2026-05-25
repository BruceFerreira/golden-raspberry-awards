package com.outsera.goldenraspberry.domain.model;

import java.util.Arrays;
import java.util.List;

public record ProducerName(String value) {

    public static List<ProducerName> parseFrom(String producers) {
        if (producers == null || producers.isBlank()) {
            return List.of();
        }
        return Arrays.stream(producers.replace(" and ", ",").split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(ProducerName::new)
                .toList();
    }
}
