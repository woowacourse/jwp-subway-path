package subway.domain.fee;

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
}
