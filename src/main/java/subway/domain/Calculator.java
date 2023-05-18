package subway.domain;

import org.springframework.stereotype.Component;

@Component
public class Calculator implements FareCalculator {

    public static final Fare BASIC_FARE = new Fare(1_250);
    public static final Distance DISTANCE_FOR_BASIC_FARE = new Distance(10);
    public static final Distance FIRST_CHARGE_DISTANCE = new Distance(50);
    public static final ChargeUnit FIRST_CHARGE_UNIT = new ChargeUnit(5);
    public static final ChargeUnit SECOND_CHARGE_UNIT = new ChargeUnit(8);

    public Fare calculate(Distance distance) {
        if (distance.isShorterThan(DISTANCE_FOR_BASIC_FARE) || distance.equals(DISTANCE_FOR_BASIC_FARE)) {
            return BASIC_FARE;
        }

        if (distance.isShorterThan(FIRST_CHARGE_DISTANCE) || distance.equals(FIRST_CHARGE_DISTANCE)) {
            Distance additionalDistance = distance.minus(DISTANCE_FOR_BASIC_FARE);
            Fare firstOverFare = BASIC_FARE.plus(calculateOverFare(additionalDistance, FIRST_CHARGE_UNIT));
            return firstOverFare;
        }

        Fare firstOverFare = BASIC_FARE.plus(
                calculateOverFare(FIRST_CHARGE_DISTANCE.minus(DISTANCE_FOR_BASIC_FARE), FIRST_CHARGE_UNIT));
        Distance secondaryAdditionalDistance = distance.minus(FIRST_CHARGE_DISTANCE);
        return firstOverFare.plus(calculateOverFare(secondaryAdditionalDistance, SECOND_CHARGE_UNIT));
    }

    private Fare calculateOverFare(Distance distance, ChargeUnit unit) {
        int fareValue = (int) ((Math.ceil((distance.getValue() - 1) / unit.getValue()) + 1) * 100);
        return new Fare(fareValue);
    }
}
