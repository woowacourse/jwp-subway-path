package subway.domain.fare.policy;

import java.util.List;
import subway.domain.fare.FareInformation;

public class FarePolicies implements FarePolicy {

    private static final int DEFAULT_FEE = 1250;

    private final List<FarePolicy> feePolicies;

    public FarePolicies(final List<FarePolicy> feePolicies) {
        this.feePolicies = feePolicies;
    }

    @Override
    public int calculate(final FareInformation fareInformation) {
        int fee = DEFAULT_FEE;
        for (FarePolicy farePolicy : feePolicies) {
            fee += farePolicy.calculate(fareInformation);
        }

        return fee;
    }
}
