package subway.domain.price;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    public static final Price ZERO = new Price(BigDecimal.ZERO);

    private final BigDecimal amount;

    private Price(BigDecimal amount) {
        this.amount = amount;
    }

    public static Price from(long amount) {
        validateNegative(amount);
        if (amount == 0) {
            return ZERO;
        }
        return new Price(BigDecimal.valueOf(amount));
    }

    private static void validateNegative(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("금액은 0 이상이어야 합니다.");
        }
    }

    public Price plus(Price price) {
        if (price == ZERO) {
            return this;
        }
        return new Price(this.amount.add(price.amount));
    }

    public Price minus(Price price) {
        validateOverPrice(price);
        if (price == ZERO) {
            return this;
        }
        if (Objects.equals(this, price)) {
            return ZERO;
        }
        return new Price(this.amount.subtract(price.amount));
    }

    private void validateOverPrice(Price price) {
        if (this.amount.compareTo(price.amount) < 0) {
            throw new IllegalArgumentException("현재 금액보다 더 큰 금액은 뺄 수 없습니다.");
        }
    }

    public Price multiple(double factor) {
        if (factor == 0) {
            return ZERO;
        }
        return new Price(this.amount.multiply(BigDecimal.valueOf(factor)));
    }

    public long getAmount() {
        return amount.longValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }

        Price price = (Price) o;

        return Objects.equals(amount, price.amount);
    }

    @Override
    public int hashCode() {
        return amount != null ? amount.hashCode() : 0;
    }
}
