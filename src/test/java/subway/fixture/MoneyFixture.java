package subway.fixture;

import subway.domain.vo.Money;

public abstract class MoneyFixture {

    public static Money 금액(final String 금액) {
        return Money.from(금액);
    }
}
