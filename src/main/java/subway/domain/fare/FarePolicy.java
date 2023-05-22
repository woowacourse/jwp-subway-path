package subway.domain.fare;

import java.util.Arrays;
import java.util.function.Function;

public enum FarePolicy {

    DEFAULT(0, 10, 1_250, (distance) -> 0),
    MIDDLE_DISTANCE(10, 50, DEFAULT.defaultFare, (extraDistance) -> calculateExtraFare(extraDistance, 5)),
    LONG_DISTANCE(50, Integer.MAX_VALUE, 2_050, (extraDistance) -> calculateExtraFare(extraDistance, 8)),;

    private final int minDistance;

    private final int maxDistance;
    private final int defaultFare;
    private final Function<Integer, Integer> extraFareByDistance;

    FarePolicy(final int minDistance,
               final int maxDistance,
               final int defaultFare,
               final Function<Integer, Integer> extraFareByDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.defaultFare = defaultFare;
        this.extraFareByDistance = extraFareByDistance;
    }

    public static FarePolicy of(int distance) {
        return Arrays.stream(values())
                .filter(farePolicy -> farePolicy.coversRangeOf(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 거리에는 운임이 부과될 수 없습니다."));
    }

    public Fare calculateFare(int distance) {
        final Integer extraFare = extraFareByDistance.apply(distance - minDistance);
        return new Fare(defaultFare + extraFare);
    }

    private boolean coversRangeOf(int distance) {
        return distance > minDistance && distance <= maxDistance;
    }

    private static Integer calculateExtraFare(final int extraDistance, final int extraFareDistanceUnit) {
        return (int) Math.ceil((double) extraDistance / extraFareDistanceUnit) * 100;
    }

}
