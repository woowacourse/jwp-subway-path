package subway.domain;

import java.math.BigDecimal;
import java.util.Objects;
import subway.exception.IllegalFareException;

public class Fare {
    private static final BigDecimal MIN_PRICE = BigDecimal.ZERO;
    private final BigDecimal price;

    private Fare(BigDecimal price) {
        this.price = price;
    }

    public static Fare from(BigDecimal price) {
        validate(price);
        return new Fare(price);
    }

    private static void validate(BigDecimal price) {
        if (price.compareTo(MIN_PRICE) < 0) {
            throw new IllegalFareException("금액은 0원보다 작을 수 없습니다.");
        }
    }

    public Fare add(Fare other) {
        return new Fare(this.price.add(other.price));
    }

    public Fare multiply(BigDecimal value) {
        return new Fare(this.price.multiply(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return price.compareTo(fare.price) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

    public BigDecimal price() {
        return this.price;
    }
}
