package subway.domain.fare;

import java.util.List;

import subway.domain.Sections;

public class FareCalculator {

    private final static int BASE_FARE = 1250;
    private final List<FarePolicy> additionalPolicies = List.of(new DistanceFarePolicy());

    public int calculate(Sections sections) {
        int fare = BASE_FARE;
        for (FarePolicy additionalPolicy : additionalPolicies) {
            fare += additionalPolicy.calculate(sections);
        }
        return fare;
    }
}