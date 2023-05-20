package subway.fixture;

import subway.domain.vo.Money;

public abstract class MoneyFixture {

    public static Money 비용(final String 비용) {
        return Money.from(비용);
    }
}
