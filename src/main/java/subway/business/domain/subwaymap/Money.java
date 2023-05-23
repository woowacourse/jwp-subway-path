package subway.business.domain.subwaymap;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private final BigDecimal money;

    public Money(final BigDecimal money) {
        validate(money);
        this.money = money;
    }

    public static Money from(final String money) {
        return new Money(new BigDecimal(money));
    }

    public Money add(final Money moneyToAdd) {
        return new Money(money.add(moneyToAdd.money));
    }

    public Money multiply(final BigDecimal moneyToMultiply) {
        return new Money(money.multiply(moneyToMultiply));
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
        final Money money = (Money) o;
        return Objects.equals(this.money, money.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
