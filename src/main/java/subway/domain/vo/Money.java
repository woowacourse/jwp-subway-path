package subway.domain.vo;

import java.util.Objects;

public class Money {

    private final double value;

    private Money(final double value) {
        this.value = value;
    }

    public static Money from(final double value) {
        return new Money(value);
    }

    public Money plus(final Money other) {
        return new Money(value + other.value);
    }

    public Money multiply(final int value) {
        return new Money(this.value * value);
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

    @Override
    public String
    toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }
}
