package subway.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    private final BigDecimal value;

    private Money(final BigDecimal value) {
        validateNegative(value);
        this.value = value;
    }

    public Money(final double value) {
        this(BigDecimal.valueOf(value));
    }

    private void validateNegative(final BigDecimal value) {
        if (value.doubleValue() < 0) {
            throw new IllegalArgumentException("돈은 음수가 될 수 없습니다.");
        }
    }

    public Money minus(final int value) {
        return new Money(this.value.subtract(BigDecimal.valueOf(value)));
    }

    public Money calculateDiscountedPrice(final int percentage) {
        final int discountedPercentage = 100 - percentage;
        return new Money(this.value.multiply(BigDecimal.valueOf(discountedPercentage))
                                   .divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN));
    }

    public Money add(final BigDecimal value) {
        return new Money(this.value.add(value));
    }

    public Money add(final Money money) {
        return new Money(this.value.add(money.value));
    }

    public Money max(final Money other) {
        return new Money(this.value.max(other.value));
    }

    public double getValue() {
        return value.doubleValue();
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
}
