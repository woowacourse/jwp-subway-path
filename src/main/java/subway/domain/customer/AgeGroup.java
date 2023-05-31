package subway.domain.customer;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeGroup {

    ADULT(age -> (Constants.MIN_ADULT_AGE <= age) && (age <= Constants.MAX_ADULT_AGE), 0, 0),
    TEENAGER(age -> (Constants.MIN_TEENAGER_AGE <= age) && (age <= Constants.MAX_TEENAGER_AGE), 350, 0.2),
    CHILD(age -> (Constants.MIN_CHILD_AGE <= age) && (age <= Constants.MAX_CHILD_AGE), 350, 0.5),
    PREFERENTIAL(age -> (age <= Constants.MAX_BABY_AGE) || (age >= Constants.MIN_OLD_AGE), 0, 1);

    private final Predicate<Integer> condition;
    private final int deductPrice;
    private final double discountRate;

    AgeGroup(Predicate<Integer> condition, int deductPrice, double discountRate) {
        this.condition = condition;
        this.deductPrice = deductPrice;
        this.discountRate = discountRate;
    }

    public static AgeGroup from(int age) {
        return Arrays.stream(values())
                .filter(value -> value.condition.test(age))
                .findAny()
                .orElseThrow();
    }

    public int getDeductPrice() {
        return deductPrice;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    private static class Constants {
        public static final int MIN_ADULT_AGE = 19;
        public static final int MAX_ADULT_AGE = 64;
        public static final int MIN_TEENAGER_AGE = 13;
        public static final int MAX_TEENAGER_AGE = 18;
        public static final int MIN_CHILD_AGE = 6;
        public static final int MAX_CHILD_AGE = 12;
        public static final int MAX_BABY_AGE = 5;
        public static final int MIN_OLD_AGE = 65;
    }
}