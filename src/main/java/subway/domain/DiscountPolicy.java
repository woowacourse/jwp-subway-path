package subway.domain;

public enum DiscountPolicy {
    NO_DISCOUNT(1.0),
    TEENAGE_DISCOUNT(0.8),
    CHILD_DISCOUNT(0.5);
    
    private final static int DEDUCT = 350;
    private final double discountRate;
    
    DiscountPolicy(final double discountRate) {
        this.discountRate = discountRate;
    }
    
    public double getDiscountRate() {
        return this.discountRate;
    }
}
