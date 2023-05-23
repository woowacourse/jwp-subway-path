package subway.line.domain.fare.dto;

import subway.line.domain.fare.Fare;
import subway.line.domain.fare.application.domain.Age;
import subway.line.domain.section.domain.Distance;

import java.util.List;

public class CustomerCondition {
    private final Distance distance;
    private final List<Fare> surcharges;
    private final Age age;

    public CustomerCondition(Distance distance, List<Fare> surcharges, Age age) {
        this.distance = distance;
        this.surcharges = surcharges;
        this.age = age;
    }

    public Distance getDistance() {
        return distance;
    }

    public List<Fare> getSurcharges() {
        return surcharges;
    }

    public Age getAge() {
        return age;
    }
}
