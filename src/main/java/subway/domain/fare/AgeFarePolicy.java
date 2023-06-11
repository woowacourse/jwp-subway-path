package subway.domain.fare;

public enum AgeFarePolicy {

    ADULT(0, 0.0),
    TEENAGER(350, 0.2),
    CHILD(350, 0.5),
    BABY(0, 1.0),
    ;

    private final int deductionFare;
    private final double discountRatio;

    AgeFarePolicy(final int deductionFare, final double discountRatio) {
        this.deductionFare = deductionFare;
        this.discountRatio = discountRatio;
    }

    public static AgeFarePolicy from(final int age) {
        if (age >= 19) {
            return ADULT;
        }
        if (age >= 13) {
            return TEENAGER;
        }
        if (age >= 6) {
            return CHILD;
        }
        return BABY;
    }

    public int calculate(final int fare) {
        return (int) ((fare - deductionFare) * (1 - discountRatio));
    }
}
