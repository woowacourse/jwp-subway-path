package subway.domain.fare;

import java.util.Arrays;
import subway.exception.InvalidAgeException;

public enum AgeGroup {
    ADULT(19, Integer.MAX_VALUE, 0, 0),
    YOUTH(13, 18, 350, 0.2),
    CHILD(6, 12, 350, 0.5),
    BABY(0, 5, 0, 1.0),
    ;

    private final int minAge;
    private final int maxAge;
    private final int discountFare;
    private final double discountRatio;

    AgeGroup(final int minAge, final int maxAge, final int discountFare, final double discountRatio) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountFare = discountFare;
        this.discountRatio = discountRatio;
    }

    public static AgeGroup from(final int age) {
        return Arrays.stream(values())
                .filter(ageGroup -> ageGroup.minAge <= age && age <= ageGroup.maxAge)
                .findFirst()
                .orElseThrow(InvalidAgeException::new);
    }

    public int getDiscountFare() {
        return discountFare;
    }

    public double getDiscountRatio() {
        return discountRatio;
    }
}
