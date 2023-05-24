package subway.application.fare;

import subway.domain.Distance;

public class FareCondition {

    private final Distance distance;

    private FareCondition(Distance distance) {
        this.distance = distance;
    }

    public static FareCondition from(Distance distance) {
        return new FareCondition(distance);
    }

    public Distance getDistance() {
        return distance;
    }
}
