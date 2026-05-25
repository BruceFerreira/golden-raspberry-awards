package com.outsera.goldenraspberry.adapter.web.response;

public record ProducerIntervalResponse(
        String producer,
        int interval,
        int previousWin,
        int followingWin
) {}
