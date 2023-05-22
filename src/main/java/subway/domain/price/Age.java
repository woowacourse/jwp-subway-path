package subway.domain.price;

import subway.exeption.InvalidAgeException;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

public enum Age {

    ADULT(age -> age >= 19,
            price -> price),
    TEENAGER(age -> age >= 13 && age < 19,
            price -> (int) ((price - 350) * 0.8)),
    CHILD(age -> age >= 6 && age < 13,
            price -> (int) ((price - 350) * 0.5)),
    BABY(age -> age >= 0 && age < 6,
            price -> 0);

    private final IntPredicate ageRange;
    private final IntUnaryOperator priceCalculator;

    Age(IntPredicate ageRange, IntUnaryOperator priceCalculator) {
        this.ageRange = ageRange;
        this.priceCalculator = priceCalculator;
    }

    public static Age of(int age) {
        return Arrays.stream(Age.values())
                .filter(range -> range.ageRange.test(age))
                .findFirst()
                .orElseThrow(() -> new InvalidAgeException("해당 나이는 유효하지 않습니다."));
    }

    public int calculatePrice(int basePrice) {
        return priceCalculator.applyAsInt(basePrice);
    }
}
