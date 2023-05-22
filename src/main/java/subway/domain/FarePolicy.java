package subway.domain;

public class FarePolicy {
    
    private static final int BASE_FARE = 1250;
    private static final int BASE_DISTANCE = 10;
    private static final int ADDITIONAL_DISTANCE = 5;
    private static final int ADDITIONAL_DISTANCE_BOUNDARY = 50;
    private static final int OVER_ADDITIONAL_DISTANCE = 8;
    private static final int ADDITIONAL_FARE = 100;
    
    private final DiscountPolicy discountPolicy;
    
    public FarePolicy(final DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }
    
    public int calculateFare(final int distance) {
        if (distance <= BASE_DISTANCE) {
            return BASE_FARE;
        }
        if (distance <= ADDITIONAL_DISTANCE_BOUNDARY) {
            return BASE_FARE + this.calculateAdditionalFare(distance);
        }
        return BASE_FARE + this.calculateAdditionalFare(ADDITIONAL_DISTANCE_BOUNDARY)
                + this.calculateOverAdditionalFare(distance);
    }
    
    private int calculateOverAdditionalFare(final int distance) {
        return (int) ((double) ((distance - ADDITIONAL_DISTANCE_BOUNDARY - 1) / OVER_ADDITIONAL_DISTANCE) + 1)
                * ADDITIONAL_FARE;
    }
    
    private int calculateAdditionalFare(final int distance) {
        return (int) ((double) ((distance - BASE_DISTANCE - 1) / ADDITIONAL_DISTANCE) + 1) * ADDITIONAL_FARE;
    }
}
