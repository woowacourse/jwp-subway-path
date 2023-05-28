package subway.fixture;

import subway.domain.fare.expense.LineExpense;
import subway.domain.vo.Distance;
import subway.domain.vo.Money;

public abstract class LineExpenseFixture {

    public static LineExpense 노선_추가_비용(final Long 노선_추가_비용_식별자값, final String 금액, final Integer 거리당, final Long 노선_식별자값) {
        return new LineExpense(노선_추가_비용_식별자값, Money.from(금액), Distance.from(거리당), 노선_식별자값);
    }
}
