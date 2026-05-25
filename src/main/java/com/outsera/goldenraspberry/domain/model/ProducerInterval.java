package com.outsera.goldenraspberry.domain.model;

public record ProducerInterval(String producer, int interval, int previousWin, int followingWin) {}
