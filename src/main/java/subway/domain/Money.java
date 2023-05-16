package subway.domain;

import java.util.Objects;

public class Money {

    private final double value;

    public Money(final double value) {
        this.value = value;
    }

    public Money plus(final Money other) {
        return new Money(value + other.value);
    }

    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Money money = (Money) o;
        return Double.compare(money.value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
