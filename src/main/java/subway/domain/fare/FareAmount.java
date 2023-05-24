package subway.domain.fare;

import java.math.BigInteger;

public class FareAmount {

    private final BigInteger amount;

    private FareAmount(final long amount) {
        validateAmount(amount);

        this.amount = BigInteger.valueOf(amount);
    }

    private void validateAmount(final long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("요금은 양수여야 합니다.");
        }
    }

    public static FareAmount from(final long amount) {
        return new FareAmount(amount);
    }

    public int getAmount() {
        return amount.intValue();
    }
}
