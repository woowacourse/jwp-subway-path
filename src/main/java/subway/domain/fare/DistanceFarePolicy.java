package subway.domain.fare;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;
import java.util.function.Predicate;
import subway.domain.line.Distance;

public enum DistanceFarePolicy {

    TEN_TO_FIFTY(
            10,
            distance -> 10 <= distance && distance < 50,
            distance -> (int) ((Math.ceil((distance - 1) / 5) + 1) * 100)
    ),
    FIFTY_TO_ONE_HUNDRED(
            50,
            distance -> 50 <= distance && distance <= 100,
            distance -> 800 + (int) ((Math.ceil((distance) / 8)) * 100)
    );

    private final int subtractDistance;
    private final Predicate<Integer> range;
    private final IntUnaryOperator operator;

    DistanceFarePolicy(int subtractDistance, Predicate<Integer> range, IntUnaryOperator operator) {
        this.subtractDistance = subtractDistance;
        this.range = range;
        this.operator = operator;
    }

    public static final int STANDARD_FARE = 1250;

    public static Fare calculateFare(Distance distance) {
        if (distance.getValue() < 10) {
            return new Fare(STANDARD_FARE);
        }

        DistanceFarePolicy distanceFarePolicy = findByDistance(distance);
        int extraDistance = distance.getValue() - distanceFarePolicy.subtractDistance;

        return new Fare(STANDARD_FARE + distanceFarePolicy.operator.applyAsInt(extraDistance));
    }

    private static DistanceFarePolicy findByDistance(Distance distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFarePolicy -> distanceFarePolicy.range.test(distance.getValue()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("거리 범위는 1 ~ 100 사이의 숫자만 가능합니다."));
    }
}
