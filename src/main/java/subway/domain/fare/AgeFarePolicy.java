package subway.domain.fare;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;

public enum AgeFarePolicy {

    BABY(
            age -> 1 <= age && age < 6,
            fare -> fare
    ),
    CHILD(
            age -> 6 <= age && age < 13,
            fare -> (int) Math.ceil((fare - 350) * 0.5)
    ),
    YOUTH(
            age -> 13 <= age && age < 19,
            fare -> (int) Math.ceil((fare - 350) * 0.2)
    ),
    ADULT(
            age -> age >= 19,
            fare -> 0
    );


    private final Predicate<Integer> range;
    private final IntUnaryOperator operator;

    AgeFarePolicy(Predicate<Integer> range, IntUnaryOperator operator) {
        this.range = range;
        this.operator = operator;
    }

    public static Fare calculateDiscountFare(Age age, Fare fare) {
        AgeFarePolicy ageFarePolicy = findByAge(age.getValue());
        int originFare = fare.getValue();
        return new Fare(ageFarePolicy.operator.applyAsInt(originFare));
    }

    private static AgeFarePolicy findByAge(int age) {
        return Arrays.stream(values())
                .filter(ageFarePolicy -> ageFarePolicy.range.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("나이에 해당하는 요금 정책이 없습니다."));
    }
}
