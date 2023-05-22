package subway.domain;

import java.math.BigDecimal;

public class FareCalculator {
    private static final Fare DEFAULT_FARE = Fare.from(BigDecimal.valueOf(1250));
    private static final Fare OVER_10_FARE = Fare.from(BigDecimal.valueOf(100));
    private static final Fare OVER_50_FARE = Fare.from(BigDecimal.valueOf(100));
    private static final Distance OVER_10KM_CHARGE_UNIT_KM = Distance.from(5);
    private static final Distance OVER_50KM_CHARGE_UNIT_KM = Distance.from(8);
    private static final Distance TEN_KM = Distance.from(10);
    private static final Distance FIFTY_KM = Distance.from(50);

    private FareCalculator() {
    }

    public static Fare calculate(Path path) {
        Distance distance = path.getDistance();
        Distance over50Distance = calculateOver50Distance(distance);
        Distance over10Distance = calculateOver10Distance(distance, over50Distance);
        return DEFAULT_FARE.add(calculateFareOver10(over10Distance)).add(calculateFareOver50(over50Distance));
    }

    private static Distance calculateOver50Distance(Distance originDistance) {
        Distance distance = originDistance.subtract(FIFTY_KM);
        if (distance.isLessThanOrEqualTo(Distance.MIN_DISTANCE)) {
            return Distance.MIN_DISTANCE;
        }
        return distance;
    }

    private static Distance calculateOver10Distance(Distance originDistance, Distance over50Distance) {
        Distance distance = originDistance.subtract(over50Distance).subtract(TEN_KM);
        if (distance.isLessThanOrEqualTo(Distance.MIN_DISTANCE)) {
            return Distance.MIN_DISTANCE;
        }
        return distance;
    }

    private static Fare calculateFareOver10(Distance distance) {
        return OVER_10_FARE.multiply(
                BigDecimal.valueOf(distance.divideAndCeil(OVER_10KM_CHARGE_UNIT_KM).value()));
    }

    private static Fare calculateFareOver50(Distance distance) {
        return OVER_50_FARE.multiply(
                BigDecimal.valueOf(distance.divideAndCeil(OVER_50KM_CHARGE_UNIT_KM).value()));
    }
}
