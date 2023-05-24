package subway.domain;

import org.springframework.stereotype.Component;

@Component
public class Calculator implements FareCalculator {

    public static final Fare BASIC_FARE = new Fare(1_250);
    public static final Distance DISTANCE_FOR_BASIC_FARE = new Distance(10);
    public static final Distance FIRST_CHARGE_DISTANCE = new Distance(50);
    public static final Distance FIRST_CHARGE_UNIT = new Distance(5);
    public static final Distance SECOND_CHARGE_UNIT = new Distance(8);
    public static final Fare FARE_1_00_WON = new Fare(100);

    public Fare calculate(Distance distance) {
        if (distance.isSameOrShorterThan(DISTANCE_FOR_BASIC_FARE)) {
            return BASIC_FARE;
        }
        if (distance.isSameOrShorterThan(FIRST_CHARGE_DISTANCE)) {
            return getFirstOverFare(distance);
        }
        return calculateSecondOverFare(distance);
    }

    private Fare getFirstOverFare(Distance distance) {
        Distance overDistance = distance.minus(DISTANCE_FOR_BASIC_FARE);
        return BASIC_FARE.addFareFor(overDistance, FIRST_CHARGE_UNIT, FARE_1_00_WON);
    }

    private Fare calculateSecondOverFare(Distance distance) {
        Distance firstOverDistance = FIRST_CHARGE_DISTANCE.minus(DISTANCE_FOR_BASIC_FARE);
        Fare afterFirstCharged = BASIC_FARE.addFareFor(firstOverDistance, FIRST_CHARGE_UNIT, FARE_1_00_WON);

        Distance secondOverDistance = distance.minus(FIRST_CHARGE_DISTANCE);
        return afterFirstCharged.addFareFor(secondOverDistance, SECOND_CHARGE_UNIT, FARE_1_00_WON);
    }
}
