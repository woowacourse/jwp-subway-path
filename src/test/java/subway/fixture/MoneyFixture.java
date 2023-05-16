package subway.fixture;

import subway.domain.Money;

public abstract class MoneyFixture {

    public static Money 비용(final double value) {
        return Money.from(value);
    }
}
