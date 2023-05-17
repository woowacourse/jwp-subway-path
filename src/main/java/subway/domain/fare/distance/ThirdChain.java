package subway.domain.fare.distance;

import subway.domain.fare.DistancePolicy;

public class ThirdChain implements DistancePolicy {
    private static final int UNIT = 8;
    private static final int FARE = 100;

    @Override
    public int calculate(final int distance) {
        return (int) ((Math.ceil((distance - 1) / UNIT) + 1) * FARE);
    }
}
