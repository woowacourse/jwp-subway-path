package subway.domain.general;

import subway.exception.IllegalMoneyException;

public class Money {
    private final long money;

    private Money(long money) {
        validate(money);
        this.money = money;
    }

    private void validate(long money) {
        if (money < 0) {
            throw new IllegalMoneyException("유효하지 않은 금액입니다. 0이상의 정수이어야 합니다.");
        }
    }

    public static Money of(long money) {
        return new Money(money);
    }

    public Money plus(Money other) {
        return new Money(this.money + other.money);
    }

    public Money minus(Money other) {
        return new Money(this.money - other.money);
    }

    public Money discount(int percentage) {
        return new Money((long)(this.money * (1 - (double)percentage/100)));
    }
    public long getMoney() {
        return money;
    }
}
