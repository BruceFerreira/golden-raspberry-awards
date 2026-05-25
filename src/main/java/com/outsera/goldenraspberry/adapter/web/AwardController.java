package com.outsera.goldenraspberry.adapter.web;

import com.outsera.goldenraspberry.adapter.web.response.AwardIntervalResponse;
import com.outsera.goldenraspberry.adapter.web.response.ProducerIntervalResponse;
import com.outsera.goldenraspberry.application.port.in.GetAwardIntervalsUseCase;
import com.outsera.goldenraspberry.domain.model.ProducerInterval;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/producers")
@RequiredArgsConstructor
public class AwardController {

    private final GetAwardIntervalsUseCase getAwardIntervalsUseCase;

    @GetMapping(value = "/awards-interval", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AwardIntervalResponse> getAwardsInterval() {
        var result = getAwardIntervalsUseCase.execute();
        return ResponseEntity.ok(new AwardIntervalResponse(toResponse(result.min()), toResponse(result.max())));
    }

    private List<ProducerIntervalResponse> toResponse(List<ProducerInterval> intervals) {
        return intervals.stream()
                .map(i -> new ProducerIntervalResponse(i.producer(), i.interval(), i.previousWin(), i.followingWin()))
                .toList();
    }
}
