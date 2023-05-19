package subway.domain.fare;

import subway.domain.line.Distance;

public class DistanceFarePolicy implements FarePolicy {

    private static final int STANDARD_FARE = 1250;

    @Override
    public Fare calculate(Distance distance) {
        if (distance.getValue() < 10) {
            return new Fare(STANDARD_FARE);
        }

        return new Fare(STANDARD_FARE + calculateOverFare(distance.getValue()));
    }

    private int calculateOverFare(int distance) {
        if (distance <= 50) {
            return calculatePhase1Fare(distance);
        }

        return calculatePhase2Fare(distance);
    }

    private int calculatePhase1Fare(int distance) {
        distance -= 10;
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculatePhase2Fare(int distance) {
        distance -= 50;
        return 800 + (int) ((Math.ceil((distance) / 8)) * 100);
    }
}
