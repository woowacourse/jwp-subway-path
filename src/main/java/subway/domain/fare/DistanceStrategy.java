package subway.domain.fare;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import subway.domain.fare.surchargescalculator.BasicSectionCalculator;
import subway.domain.fare.surchargescalculator.FirstSectionCalculator;
import subway.domain.fare.surchargescalculator.SecondSectionCalculator;
import subway.domain.fare.surchargescalculator.SurchargesCalculator;

@Component
public class DistanceStrategy implements FareStrategy {
    private final static List<SurchargesCalculator> calculators = Arrays.asList(
            new BasicSectionCalculator(),
            new FirstSectionCalculator(),
            new SecondSectionCalculator());

    @Override
    public Fare calculate(final int distance) {
        validateDistance(distance);
        SurchargesCalculator strategy = getStrategy(distance);
        return strategy.calculate(distance);
    }

    private SurchargesCalculator getStrategy(final int distance) {
        return calculators.stream()
                .filter(strategy -> strategy.isAcceptable(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("추가 요금에 대한 알맞은 정책을 찾을 수 없습니다."));
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리가 0인 잘못된 경로입니다.");
        }
    }
}
