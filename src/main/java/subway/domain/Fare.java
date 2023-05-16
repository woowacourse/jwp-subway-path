package subway.domain;

public class Fare {
    private static final Distance DEFAULT_DISTANCE_STANDARD = new Distance(10);
    private static final Distance MIDDLE_DISTANCE_STANDARD = new Distance(50);
    private static final int DEFAULT_FARE = 1250;

    private final int fare;

    public Fare(Distance distance) {
        this.fare = calculateFare(distance);
    }

    private int calculateFare(Distance distance) {
        if (distance.isLessThanOrEqualTo(DEFAULT_DISTANCE_STANDARD)) {
            return DEFAULT_FARE;
        }

        if (distance.isLessThanOrEqualTo(MIDDLE_DISTANCE_STANDARD)) {
            return DEFAULT_FARE + calculateOverFareByMiddleDistance(distance);
        }

        return DEFAULT_FARE + calculateOverFareByLongDistance(distance);
    }

    private int calculateOverFareByMiddleDistance(Distance distance) {
        return (int) ((Math.ceil((distance.getDistance() - 1) / 5) + 1) * 100);
    }


    private int calculateOverFareByLongDistance(Distance distance) {
        return (int) ((Math.ceil((distance.getDistance() - 1) / 8) + 1) * 100);
    }

    public int getFare() {
        return fare;
    }
}
