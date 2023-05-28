package subway.domain.fare.expense;

import subway.domain.fare.FareInfo;
import subway.domain.fare.FarePolicy;

import java.util.List;

public class ExpenseComposite {

    private final List<FarePolicy> farePolicies;

    public ExpenseComposite(final List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public void addExpensePolicy(final FarePolicy farePolicy) {
        farePolicies.add(farePolicy);
    }

    public FareInfo doImpose(final FareInfo fareInfo) {
        FareInfo currentFareInfo = fareInfo;
        for (final FarePolicy farePolicy : farePolicies) {
            currentFareInfo = farePolicy.doCalculate(currentFareInfo);
        }
        return currentFareInfo;
    }

    @Override
    public String toString() {
        return "ExpenseComposite{" +
                "farePolicies=" + farePolicies +
                '}';
    }
}
