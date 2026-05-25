package com.outsera.goldenraspberry.domain.model;

import java.util.List;

public record Movie(Long id, int year, String title, String studios, String producers, boolean winner) {

    public List<ProducerName> producerNames() {
        return ProducerName.parseFrom(producers);
    }
}
