package subway.line.domain.fare.application.faremeterpolicy;

import subway.line.domain.section.domain.Distance;

public class CustomerCondition {
    private final Distance distance;

    public CustomerCondition(Distance distance) {
        this.distance = distance;
    }

    public Distance getDistance() {
        return distance;
    }
}
