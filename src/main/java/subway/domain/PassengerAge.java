package subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum PassengerAge {
    BABY(age -> 0 < age && age < 6, 1, 0),
    CHILD(age -> 6 <= age && age < 13, 0.5, 350),
    TEENAGER(age -> 13 <= age && age < 19, 0.2, 350),
    ADULT(age -> age >= 19, 0, 0);

    private final Predicate<Integer> ageValidator;
    private final double discountRate;
    private final int excludedAmount;

    PassengerAge(final Predicate<Integer> ageValidator, final double discountRate, final int excludedAmount) {
        this.ageValidator = ageValidator;
        this.discountRate = discountRate;
        this.excludedAmount = excludedAmount;
    }

    public static PassengerAge get(final int age) {
        return Arrays.stream(values())
            .filter((passengerAge -> passengerAge.ageValidator.test(age)))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("나이는 1보다 작을 수 없습니다"));
    }

    public int getDiscountedPrice(final int fare) {
        return (int) (fare - (fare - excludedAmount) * discountRate);
    }
}
