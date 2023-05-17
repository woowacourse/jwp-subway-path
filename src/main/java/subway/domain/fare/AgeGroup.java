package subway.domain.fare;

public enum AgeGroup {
    ADULT(0, 0),
    YOUTH(350, 0.2),
    CHILD(350, 0.5),
    ;

    private final int discountFare;
    private final double discountRatio;

    AgeGroup(final int discountFare, final double discountRatio) {
        this.discountFare = discountFare;
        this.discountRatio = discountRatio;
    }

    public int calculate(final int fare) {
        final int result = fare - discountFare;
        return (int) (result - result * discountRatio);
    }
}
