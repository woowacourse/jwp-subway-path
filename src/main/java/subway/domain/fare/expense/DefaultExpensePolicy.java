package subway.domain.fare.expense;

import subway.domain.fare.FareInfo;
import subway.domain.fare.FarePolicy;
import subway.domain.vo.Money;

public class DefaultExpensePolicy implements FarePolicy {

    private static final Money DEFAULT_EXPENSE = Money.from("1250");

    @Override
    public FareInfo doCalculate(final FareInfo fareInfo) {
        return fareInfo.plusFare(DEFAULT_EXPENSE);
    }
}
