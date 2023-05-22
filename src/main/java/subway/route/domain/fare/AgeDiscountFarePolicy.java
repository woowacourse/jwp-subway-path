package subway.route.domain.fare;

import subway.route.domain.RouteSegment;
import subway.route.exception.InvalidAgeException;

import java.util.List;
import java.util.Objects;

public class AgeDiscountFarePolicy implements FarePolicy {

    private static final String AGE_FARE_FACTOR = "age";
    private static final int CHILD_MINIMUM_AGE = 6;
    private static final int TEENAGER_MINIMUM_AGE = 13;
    private static final int ADULT_MINIMUM_AGE = 20;
    private static final int DEDUCTION = 350;
    private static final double CHILD_SCALE_RATIO = 0.5;
    private static final double TEENAGER_SCALE_RATIO = 0.8;

    @Override
    public void buildFareFactors(FareFactors fareFactors, List<RouteSegment> route, int distance, int age) {
        fareFactors.setFactor(AGE_FARE_FACTOR, age);
    }

    @Override
    public int calculate(FareFactors fareFactors, int fare) {
        final Integer age = (Integer) fareFactors.getFactor(AGE_FARE_FACTOR);
        validateAge(age);

        if (age < CHILD_MINIMUM_AGE) {
            return 0;
        }
        if (age < TEENAGER_MINIMUM_AGE) {
            return (int) ((fare - DEDUCTION) * CHILD_SCALE_RATIO);
        }
        if (age < ADULT_MINIMUM_AGE) {
            return (int) ((fare - DEDUCTION) * TEENAGER_SCALE_RATIO);
        }
        return fare;
    }

    private void validateAge(Integer age) {
        if (Objects.isNull(age)) {
            throw new IllegalArgumentException("buildFareFactors를 먼저 호출해야 합니다.");
        }
        if (age < 0) {
            throw new InvalidAgeException("나이는 0 이상의 값입니다. age: " +age);
        }
    }
}
