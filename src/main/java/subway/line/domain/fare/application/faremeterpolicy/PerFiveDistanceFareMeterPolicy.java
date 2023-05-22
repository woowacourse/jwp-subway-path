package subway.line.domain.fare.application.faremeterpolicy;

import org.springframework.stereotype.Component;
import subway.line.domain.fare.Fare;
import subway.line.domain.section.domain.Distance;

@Component
public class PerFiveDistanceFareMeterPolicy implements FareMeterPolicy {
    public static final Distance MIN_DISTANCE = Distance.of(10);
    public static final Distance MAX_DISTANCE = Distance.of(50);
    public static final int DEFAULT_FARE_DISTANCE = 10;
    public static final int UNIT_MEASURE_DISTANCE = 5;

    @Override
    public boolean support(CustomerCondition customerCondition) {
        final var distance = customerCondition.getDistance();
        return distance.isMoreThanOrEquals(MIN_DISTANCE) && distance.isLessThanOrEquals(MAX_DISTANCE);
    }

    @Override
    public Fare calculateFare(CustomerCondition customerCondition) {
        final var fare = new Fare();
        return fare.addSurchargeMultipliedBy(calculateMultiplyingCount(customerCondition.getDistance()));
    }

    private static double calculateMultiplyingCount(Distance distance) {
        return Math.floor((distance.getValue() - DEFAULT_FARE_DISTANCE) / UNIT_MEASURE_DISTANCE);
    }
}
