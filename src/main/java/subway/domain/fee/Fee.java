package subway.domain.fee;

import java.util.Objects;

public final class Fee {

    private static final Fee ZERO_FEE = new Fee(0);

    private final int amount;

    public Fee(final int amount) {
        this.amount = amount;
    }

    public static Fee initWithZero() {
        return ZERO_FEE;
    }

    public Fee add(final Fee fee) {
        return new Fee(amount + fee.amount);
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Fee)) return false;
        Fee fee = (Fee) o;
        return amount == fee.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
