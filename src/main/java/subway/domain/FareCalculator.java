package subway.domain;

import org.springframework.stereotype.Component;

@Component
public class FareCalculator {
    private static final int DEFAULT_FARE = 1250;
    private static final int OVER_10_FARE = 100;
    private static final int OVER_50_FARE = 100;
    private static final Distance OVER_10KM_CHARGE_UNIT_KM = Distance.from(5);
    private static final Distance OVER_50KM_CHARGE_UNIT_KM = Distance.from(8);
    private static final Distance TEN_KM = Distance.from(10);
    private static final Distance FIFTY_KM = Distance.from(50);


    public int calculate(Path path) {
        Distance distance = path.getDistance();
        Distance over50Distance = calculateOver50Distance(distance);
        Distance over10Distance = calculateOver10Distance(distance, over50Distance);
        return DEFAULT_FARE + calculateFareOver10(over10Distance) + calculateFareOver50(over50Distance);
    }

    private Distance calculateOver50Distance(Distance originDistance) {
        Distance distance = originDistance.subtract(FIFTY_KM);
        if (distance.isLessThanOrEqualTo(Distance.MIN_DISTANCE)) {
            return Distance.MIN_DISTANCE;
        }
        return distance;
    }

    private Distance calculateOver10Distance(Distance originDistance, Distance over50Distance) {
        Distance distance = originDistance.subtract(over50Distance).subtract(TEN_KM);
        if (distance.isLessThanOrEqualTo(Distance.MIN_DISTANCE)) {
            return Distance.MIN_DISTANCE;
        }
        return distance;
    }

    private int calculateFareOver10(Distance distance) {
        return (int) (distance.divideAndCeil(OVER_10KM_CHARGE_UNIT_KM).value() * OVER_10_FARE);
    }

    private int calculateFareOver50(Distance distance) {
        return (int) (distance.divideAndCeil(OVER_50KM_CHARGE_UNIT_KM).value() * OVER_50_FARE);
    }
}
