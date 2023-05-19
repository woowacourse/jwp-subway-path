package subway.route.domain.fare;

import subway.route.domain.RouteSegment;

import java.util.List;

public class FarePoliciesComposite implements FarePolicy {

    private final List<FarePolicy> farePolicies;

    public FarePoliciesComposite(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    @Override
    public void buildFareFactors(FareFactors fareFactors, List<RouteSegment> route, int distance, int age) {
        for (FarePolicy farePolicy : farePolicies) {
            farePolicy.buildFareFactors(fareFactors, route, distance, age);
        }
    }

    @Override
    public int calculate(FareFactors fareFactors, int fare) {
        for (FarePolicy farePolicy : farePolicies) {
            fare = farePolicy.calculate(fareFactors, fare);
        }

        return fare;
    }
}
