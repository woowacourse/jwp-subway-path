package subway.domain.fare;

import java.util.List;

import org.springframework.stereotype.Component;
import subway.domain.fare.strategy.FareStrategy;
import subway.domain.section.Distance;

@Component
public class SubwayFareCalculator implements FareCalculator {

    public static final Fare DEFAULT_FARE = new Fare(1250);
    public static final Distance DEFAULT_DISTANCE = new Distance(10);
    public static final Distance DISTANCE_THRESHOLD = new Distance(50);
    public static final Fare ADDITIONAL_FARE = new Fare(100);
    public static final int INITIAL_INCREASE_DISTANCE = 5;
    public static final int SECONDARY_INCREASE_DISTANCE = 8;
    public static final int COUNT_TO_INITIAL_INCREASE = 8;

    private final List<FareStrategy> fareStrategies;

    public SubwayFareCalculator(final List<FareStrategy> fareStrategies) {
        this.fareStrategies = fareStrategies;
    }

    @Override
    public Fare calculate(final Distance distance) {
        return fareStrategies.stream()
                .filter(strategy -> strategy.isApplicable(distance))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("해당하는 요금 정책이 없습니다."))
                .calculate(distance);
    }
}
