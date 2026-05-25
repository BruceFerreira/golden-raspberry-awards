package com.outsera.goldenraspberry.application.port.in;

import com.outsera.goldenraspberry.domain.model.ProducerInterval;

import java.util.List;

public record AwardIntervalResult(List<ProducerInterval> min, List<ProducerInterval> max) {}
