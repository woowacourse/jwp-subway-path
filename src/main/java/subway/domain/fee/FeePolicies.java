package subway.domain.fee;

import java.util.List;

public class FeePolicies implements FeePolicy {

    private static final int DEFAULT_FEE = 1250;
    private final List<FeePolicy> feePolicies;

    public FeePolicies(List<FeePolicy> feePolicies) {
        this.feePolicies = feePolicies;
    }

    @Override
    public int calculate(FeeInformation feeInformation) {
        int fee = DEFAULT_FEE;
        for (FeePolicy feePolicy : feePolicies) {
            fee += feePolicy.calculate(feeInformation);
        }

        return fee;
    }
}
