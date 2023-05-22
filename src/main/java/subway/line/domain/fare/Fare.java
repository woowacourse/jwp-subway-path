package subway.line.domain.fare;

import java.math.BigDecimal;

public class Fare {
    private static final BigDecimal DEFAULT_FARE = new BigDecimal("1250");
    private static final BigDecimal SURCHARGE = new BigDecimal("100");
    private final BigDecimal money;

    public Fare() {
        this.money = DEFAULT_FARE;
    }

    private Fare(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public Fare addSurchargeMultipliedBy(double times) {
        return new Fare(DEFAULT_FARE.add(SURCHARGE.multiply(BigDecimal.valueOf(times))));
    }
}
