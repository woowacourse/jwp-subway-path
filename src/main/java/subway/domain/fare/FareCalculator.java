package subway.domain.fare;

import subway.domain.path.Path;

import java.util.List;

public class FareCalculator {

    private final List<FarePolicy> farePolicies;

    public FareCalculator(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public int calculate(Path path) {
        return farePolicies.stream()
                .mapToInt(farePolicy -> farePolicy.calculateFare(path))
                .sum();
    }
}
