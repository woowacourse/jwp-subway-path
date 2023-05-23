package subway.domain;

import subway.exception.ApiIllegalStateException;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.LongUnaryOperator;

public enum AgePolicy {

    ADULT(age -> age >= 19,
            price -> price),
    TEENAGER(age -> age >= 13 && age < 19,
            price -> (int) ((price - Constants.BASE_DISCOUNT_COST) * Constants.TEENAGER_DISCOUNT_RATE)),
    CHILD(age -> age >= 6 && age < 13,
            price -> (int) ((price - Constants.BASE_DISCOUNT_COST) * Constants.CHILDREN_DISCOUNT_RATE)),
    BABY(age -> age >= 0 && age < 6,
            price -> 0);

    private final IntPredicate ageRange;
    private final LongUnaryOperator priceCalculator;

    AgePolicy(IntPredicate ageRange, LongUnaryOperator priceCalculator) {
        this.ageRange = ageRange;
        this.priceCalculator = priceCalculator;
    }

    public static AgePolicy from(int age) {
        return Arrays.stream(AgePolicy.values())
                .filter(range -> range.ageRange.test(age))
                .findAny()
                .orElseThrow(() -> new ApiIllegalStateException("해당 나이는 유효하지 않습니다."));
    }

    public long applyPolicy(long basePrice) {
        return priceCalculator.applyAsLong(basePrice);
    }

    private static class Constants {
        private static final int BASE_DISCOUNT_COST = 350;
        private static final double TEENAGER_DISCOUNT_RATE = 0.8;
        private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    }
}
