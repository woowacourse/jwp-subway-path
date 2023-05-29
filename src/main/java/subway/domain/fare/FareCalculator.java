package subway.domain.fare;

import subway.domain.line.Line;

import java.util.List;
import java.util.Set;

public class FareCalculator {

    private final List<FarePolicy> farePolicies;

    public FareCalculator(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public int calculate(int distance, Set<Line> linesToUse) {
        return farePolicies.stream()
                .mapToInt(farePolicy -> farePolicy.calculateFare(distance, linesToUse))
                .sum();
    }
}
