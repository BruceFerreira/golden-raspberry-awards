package com.outsera.goldenraspberry.adapter.web.response;

import java.util.List;

public record AwardIntervalResponse(
        List<ProducerIntervalResponse> min,
        List<ProducerIntervalResponse> max
) {}
