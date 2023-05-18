package subway.domain;

import org.springframework.stereotype.Component;
import subway.application.FarePolicy;
import subway.domain.general.Distance;
import subway.domain.general.Money;

@Component
public class BasicDistanceProportionalPolicy implements FarePolicy {

    public static final int BASIC_DISTANCE = 10;
    public static final int ADDITIONAL_FARE = 100;
    public static final int BASIC_FARE = 1250;
    public static final int SECOND_HURDLE_DISTANCE = 50;

    @Override
    public Money getFareFrom(double movedDistance) {
        Money fare = Money.of(BASIC_FARE);
        Distance distance = Distance.of(movedDistance);

        if (distance.isSameOrOver(BASIC_DISTANCE)) {
            int overCount = calculateOverCount(5, (int) (distance.getDistance() - BASIC_DISTANCE));
            if (overCount > 8) {
                overCount = 8;
            }
            fare = fare.plus(Money.of(overCount * ADDITIONAL_FARE));
        }

        if (distance.isOver(SECOND_HURDLE_DISTANCE)) {
            int overCount = calculateOverCount(8, (int) (distance.getDistance() - SECOND_HURDLE_DISTANCE));
            fare = fare.plus(Money.of(overCount * ADDITIONAL_FARE));
        }
        return fare;
    }

    private int calculateOverCount(int distanceUnit, int distance) {
        return (int) (Math.ceil((distance - 1) / distanceUnit) + 1);
    }
}
