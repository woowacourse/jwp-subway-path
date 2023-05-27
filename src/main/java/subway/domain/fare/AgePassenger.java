package subway.domain.fare;

import java.util.Arrays;

public enum AgePassenger {
    CHILD(6, 12, 0.5),
    TEENAGER(13, 18, 0.8),
    ;

    private final int lowerBoundAge;
    private final int upperBoundAge;
    private final double discountProportion;

    AgePassenger(final int lowerBoundAge, final int upperBoundAge, final double discountProportion) {
        this.lowerBoundAge = lowerBoundAge;
        this.upperBoundAge = upperBoundAge;
        this.discountProportion = discountProportion;
    }

    public static double findDiscountProportionByAge(final int age) {
        return Arrays.stream(AgePassenger.values())
                .filter(agePassenger -> isInRange(age, agePassenger))
                .map(AgePassenger::getDiscountProportion)
                .findFirst().orElse(1d);
    }

    private static boolean isInRange(final int age, final AgePassenger agePassenger) {
        return age <= agePassenger.upperBoundAge && age >= agePassenger.lowerBoundAge;
    }

    public double getDiscountProportion() {
        return discountProportion;
    }
}
