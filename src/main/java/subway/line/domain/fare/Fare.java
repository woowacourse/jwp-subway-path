package subway.line.domain.fare;

import java.math.BigDecimal;
import java.util.Objects;

public class Fare implements Comparable<Fare> {
    private static final BigDecimal DEFAULT_FARE = new BigDecimal("1250");
    private static final BigDecimal SURCHARGE = new BigDecimal("100");
    private final BigDecimal money;

    public Fare() {
        this.money = DEFAULT_FARE;
    }

    public Fare(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public Fare addSurchargeMultipliedBy(double times) {
        return new Fare(money.add(SURCHARGE.multiply(BigDecimal.valueOf(times))));
    }

    public Fare add(Fare fare) {
        return new Fare(money.add(fare.money));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return Objects.equals(money, fare.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }

    @Override
    public int compareTo(Fare o) {
        return this.money.compareTo(o.money);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "money=" + money +
                '}';
    }

    public Fare subtract(Fare fare) {
        return new Fare(this.money.subtract(fare.money));
    }

    public Fare discountPercentage(double percentage) {
        return new Fare(this.money.multiply(new BigDecimal(1 - percentage)));
    }
}
