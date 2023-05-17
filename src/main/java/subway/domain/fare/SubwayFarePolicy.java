package subway.domain.fare;

import java.util.List;
import subway.domain.path.PathFindResult;

public class SubwayFarePolicy implements FarePolicy {

    private final List<FarePolicy> farePolicies;

    public SubwayFarePolicy(final List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    @Override
    public int calculate(final PathFindResult result, final Passenger passenger, final int fare) {
        int calculatedFare = fare;
        for (FarePolicy farePolicy : farePolicies) {
            calculatedFare = farePolicy.calculate(result, passenger, calculatedFare);
        }
        return calculatedFare;
    }
}
