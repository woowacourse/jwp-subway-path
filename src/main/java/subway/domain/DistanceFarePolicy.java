package subway.domain;

public class DistanceFarePolicy {

    private static final Long STANDARD_LOWER_DISTANCE = 10L;
    private static final Long STANDARD_UPPER_DISTANCE = 50L;

    public int calculate(final Long distance, final int fare) {
        if (distance > STANDARD_LOWER_DISTANCE && distance <= STANDARD_UPPER_DISTANCE) {
            return fare + calculateOverFareWhenNotExceedUpperDistance(distance - STANDARD_LOWER_DISTANCE);
        }
        if (distance > STANDARD_UPPER_DISTANCE) {
            return fare
                    + calculateOverFareWhenNotExceedUpperDistance(STANDARD_UPPER_DISTANCE - STANDARD_LOWER_DISTANCE)
                    + calculateOverFareWhenExceedUpperDistance(distance - STANDARD_UPPER_DISTANCE);
        }
        return fare;
    }

    private int calculateOverFareWhenNotExceedUpperDistance(Long overDistance) {
        return (int) ((((overDistance - 1) / 5) + 1) * 100);
    }

    private int calculateOverFareWhenExceedUpperDistance(Long overDistance) {
        return (int) ((((overDistance - 1) / 8) + 1) * 100);
    }
}
