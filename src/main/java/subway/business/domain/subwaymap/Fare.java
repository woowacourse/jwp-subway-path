package subway.business.domain.subwaymap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import subway.business.domain.line.Line;

public class Fare {

    private static final int DEFAULT_DISTANCE_LIMIT = 10;
    private static final int DEFAULT_FARE = 1_250;

    private static final int FIRST_INCREASE = 100;
    private static final int FIRST_LEVEL_DISTANCE_LIMIT = 50;
    private static final int FIRST_STEP = 5;

    private static final int SECOND_INCREASE = 100;
    private static final int SECOND_STEP = 8;

    private final BigDecimal money;

    private Fare(final BigDecimal money) {
        validate(money);
        this.money = money;
    }

    public static Fare from(String money) {
        return new Fare(new BigDecimal(money));
    }

    public static Fare of(Line mostExpensiveLine, int distance, Passenger passenger) {
        return new Fare(calculateMoneyByDistance(distance).add(mostExpensiveLine.getSurcharge())
                .multiply(BigDecimal.valueOf(passenger.getFareRate()))
        );
    }

    private static BigDecimal calculateMoneyByDistance(int distance) {
        if (distance <= DEFAULT_DISTANCE_LIMIT) {
            return BigDecimal.valueOf(DEFAULT_FARE);
        }
        if (distance <= FIRST_LEVEL_DISTANCE_LIMIT) {
            return BigDecimal.valueOf(DEFAULT_FARE)
                    .add(BigDecimal.valueOf(FIRST_INCREASE)
                            .multiply(BigDecimal.valueOf((distance - DEFAULT_DISTANCE_LIMIT) / FIRST_STEP)));
        }
        return BigDecimal.valueOf(DEFAULT_FARE)
                .add(BigDecimal.valueOf(SECOND_INCREASE)
                        .multiply(BigDecimal.valueOf((FIRST_LEVEL_DISTANCE_LIMIT - DEFAULT_DISTANCE_LIMIT) / FIRST_STEP)
                                .add(BigDecimal.valueOf((distance - FIRST_LEVEL_DISTANCE_LIMIT) / SECOND_STEP))));
    }

    public String getMoney() {
        return money.setScale(0, RoundingMode.CEILING).toString();
    }

    private void validate(BigDecimal money) {
        if (money.doubleValue() < 0) {
            throw new IllegalArgumentException(String.format(
                    "0보다 작은 금액은 존재할 수 없습니다. "
                            + "(입력된 금액 : %s)", money.doubleValue()));
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Fare fare = (Fare) o;
        return Objects.equals(this.money, fare.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
