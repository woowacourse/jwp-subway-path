package subway.application;

import org.springframework.stereotype.Component;
import subway.domain.general.Money;

@Component
public class BasicDistanceProportionalPolicy implements FarePolicy {

    public static final int BASIC_DISTANCE = 10;
    public static final int ADDITIONAL_FARE = 100;
    public static final int BASIC_FARE = 1250;
    public static final int SECOND_HURDLE_DISTANCE = 50;
    public static final int MINIMAL_DISTANCE = 0;

    @Override
    public Money getFareFrom(double distance) {
        if (distance < MINIMAL_DISTANCE) {
            throw new IllegalArgumentException();
        }
        Money fare = Money.of(BASIC_FARE);

        if (BASIC_DISTANCE <= distance) {
            int overCount = calculateOverCount(5, (int) (distance - BASIC_DISTANCE));
            if (overCount > 8) {
                overCount = 8;
            }
            fare = fare.plus(Money.of(overCount * ADDITIONAL_FARE));
        }

        if (SECOND_HURDLE_DISTANCE < distance) {
            int overCount = calculateOverCount(8, (int) (distance - SECOND_HURDLE_DISTANCE));
            fare = fare.plus(Money.of(overCount * ADDITIONAL_FARE));
        }
        return fare;
    }

    private int calculateOverCount(int distanceUnit, int distance) {
        return (int) (Math.ceil((distance - 1) / distanceUnit) + 1);
    }
}
