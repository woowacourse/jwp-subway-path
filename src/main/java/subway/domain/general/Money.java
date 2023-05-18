package subway.domain.general;

public class Money {
    private final long money;

    private Money(long money) {
        this.money = money;
    }

    public static Money of(long money) {
        return new Money(money);
    }

    public Money plus(Money other) {
        return new Money(this.money + other.money);
    }

    public long getMoney() {
        return money;
    }
}
