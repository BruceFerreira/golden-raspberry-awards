package com.outsera.goldenraspberry.application.usecase;

import com.outsera.goldenraspberry.application.port.in.AwardIntervalResult;
import com.outsera.goldenraspberry.application.port.in.GetAwardIntervalsUseCase;
import com.outsera.goldenraspberry.domain.port.out.MovieRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetAwardIntervalsInteractor implements GetAwardIntervalsUseCase {

    private final MovieRepositoryPort movieRepository;
    private final ProducerIntervalCalculator calculator;

    @Override
    public AwardIntervalResult execute() {
        return calculator.calculate(movieRepository.findAllWinners());
    }
}
