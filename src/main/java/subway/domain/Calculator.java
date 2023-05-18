package subway.domain;

import org.springframework.stereotype.Component;

@Component
public class Calculator implements FareCalculator {

    public static final Fare BASIC_FARE = new Fare(1_250);
    public static  final Distance DISTANCE_FOR_BASIC_FARE = new Distance(10);
    public static final Distance FIFTY_KM_DISTANCE = new Distance(50);

    public Fare calculate(Distance distance) {
        if (distance.isCloserThan(DISTANCE_FOR_BASIC_FARE) || distance.equals(DISTANCE_FOR_BASIC_FARE)) {
            return BASIC_FARE;
        }

        if (distance.getValue() <= 50) {
            Distance additionalDistance = distance.minus(DISTANCE_FOR_BASIC_FARE);
            return BASIC_FARE
                    .plus(new Fare(additionalDistance.getValue() / 5 * 100));
        }

        Distance secondaryAdditionalDistance = distance.minus(FIFTY_KM_DISTANCE);
        return BASIC_FARE
                .plus(new Fare(800))
                .plus(new Fare(secondaryAdditionalDistance.getValue() / 8 * 100));
    }
}
