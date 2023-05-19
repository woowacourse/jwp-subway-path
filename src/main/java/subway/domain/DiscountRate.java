package subway.domain;

public enum DiscountRate {
    CHILD_DISCOUNT_RATE(0.5),
    TEENAGER_DISCOUNT_RATE(0.2);

    private final double value;

    DiscountRate(double value) {
        this.value = value;
    }

    public int discount(int charge) {
        return (int) Math.ceil(charge * (1 - value));
    }
}
