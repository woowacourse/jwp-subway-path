package subway.domain;

public class Charge {

    private static final Charge deductCharge = new Charge(350);
    private final int value;

    public Charge(int value) {
        this.value = value;
    }

    public Charge add(Charge charge) {
        return new Charge(value + charge.value);
    }

    public Charge multiply(Charge charge) {
        return new Charge(value * charge.value);
    }

    public Charge discount(DiscountRate discountRate) {
        return new Charge(discountRate.discount((value - deductCharge.value)));
    }

    public int getValue() {
        return value;
    }
}
