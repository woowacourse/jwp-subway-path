package subway.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum PassengerType {
    BABY(age -> 0 <= age && age < 5),
    CHILDREN(age -> 6 <= age && age < 13),
    TEENAGER(age -> 13 <= age && age < 19),
    ADULT(age -> 19 <= age);

    private final Predicate<Integer> ageRange;

    PassengerType(final Predicate<Integer> ageRange) {
        this.ageRange = ageRange;
    }

    public static PassengerType findBy(final int age) {
        return Arrays.stream(PassengerType.values())
                .filter(passengerType -> passengerType.ageRange.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("승객은 0살 이상이어야 합니다."));
    }
}
