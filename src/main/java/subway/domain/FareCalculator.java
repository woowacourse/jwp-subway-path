package subway.domain;

public class FareCalculator {

    private static final Distance OVER_FARE_LEVEL1_RANGE_MINIMUM = new Distance(10);
    private static final Distance OVER_FARE_LEVEL2_RANGE_MINIMUM = new Distance(50);
    private static final Distance OVER_FARE_LEVEL1_CHARGING_UNIT = new Distance(5);
    private static final Distance OVER_FARE_LEVEL2_CHARGING_UNIT = new Distance(8);
    private static final int BASIC_FARE = 1250;
    private static final int OVER_FARE_RATE = 100;

    private FareCalculator() {
    }

    public static int calculate(final Distance distance) {
        if (distance.isWithIn(OVER_FARE_LEVEL1_RANGE_MINIMUM)) {
            return BASIC_FARE;
        }
        if (distance.isWithIn(OVER_FARE_LEVEL2_RANGE_MINIMUM)) {
            return BASIC_FARE + overFareLevel1(distance.minus(OVER_FARE_LEVEL1_RANGE_MINIMUM));
        }
        Distance overDistanceLevel2 = distance.minus(OVER_FARE_LEVEL2_RANGE_MINIMUM);
        Distance overDistanceLevel1 = distance.minus(overDistanceLevel2)
                .minus(OVER_FARE_LEVEL1_RANGE_MINIMUM);
        return BASIC_FARE + overFareLevel1(overDistanceLevel1) + overFareLevel2(overDistanceLevel2);
    }

    private static int overFareLevel1(Distance overDistance) {
        return (overFareChargingTimes(overDistance, OVER_FARE_LEVEL1_CHARGING_UNIT) * OVER_FARE_RATE);
    }

    private static int overFareLevel2(Distance overDistance) {
        return (overFareChargingTimes(overDistance, OVER_FARE_LEVEL2_CHARGING_UNIT) * OVER_FARE_RATE);
    }

    private static int overFareChargingTimes(Distance overDistance, Distance chargingUnit) {
        return (int) (Math.ceil((overDistance.getValue() - 1) / chargingUnit.getValue()) + 1);
    }
}
