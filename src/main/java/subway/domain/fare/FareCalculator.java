package subway.domain.fare;

import java.util.List;

public class FareCalculator {

    private final int baseFare;

    private final List<FarePolicy> farePoliciesInApplicationOrder;

    public FareCalculator(int baseFare, List<FarePolicy> farePoliciesInApplicationOrder) {
        this.baseFare = baseFare;
        this.farePoliciesInApplicationOrder = farePoliciesInApplicationOrder;
    }

    public int calculate(FarePolicyRelatedParameters parameters) {
        int fare = baseFare;
        for (FarePolicy policy : farePoliciesInApplicationOrder) {
            if (policy.supports(parameters)) {
                fare = policy.calculate(fare, parameters);
            }
        }
        return fare;
    }
}
