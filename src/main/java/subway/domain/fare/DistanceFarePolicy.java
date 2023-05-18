package subway.domain.fare;

public class DistanceFarePolicy implements FarePolicy {

    private static final int BASIC_FARE = 1250;
    private static final int BASIC_DISTANCE = 10;

    private static final int PHASE1_DISTANCE_UNIT = 5;
    private static final int PHASE1_DISTANCE = 40;
    private static final int PHASE1_FARE = 800;

    private static final int PHASE2_DISTANCE_UNIT = 8;

    private static final int UNIT = 100;

    @Override
    public int calculateFare(final int distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }
        return BASIC_FARE + calculateOverFare1(distance - BASIC_DISTANCE);
    }

    private int calculateOverFare1(final int distance) {
        if (distance <= PHASE1_DISTANCE) {
            return ((distance / PHASE1_DISTANCE_UNIT) + 1) * UNIT;
        }

        return PHASE1_FARE + calculateOverFare2(distance - PHASE1_DISTANCE);
    }

    private int calculateOverFare2(final int distance) {

        return (distance / PHASE2_DISTANCE_UNIT) * UNIT;
    }
}
