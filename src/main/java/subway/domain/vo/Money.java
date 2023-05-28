package subway.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    public static final Money ZERO = new Money(new BigDecimal("0"));

    private final BigDecimal value;

    private Money(final BigDecimal value) {
        this.value = value;
    }

    public static Money from(final String value) {
        return new Money(new BigDecimal(value));
    }

    public Money plus(final Money otherValue) {
        return new Money(value.add(otherValue.value));
    }

    public Money minus(final Money otherValue) {
        return new Money(value.subtract(otherValue.value));
    }

    public Money multiply(final BigDecimal otherValue) {
        return new Money(value.multiply(otherValue));
    }

    public String getValue() {
        return value.setScale(0, RoundingMode.CEILING).toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }
}
