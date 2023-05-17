package subway.domain.fare.distance;

import subway.domain.fare.DistancePolicy;

public class SecondChain implements DistancePolicy {
    private static final int UNIT = 5;
    private static final int FARE = 100;
    private static final int RANGE = 40;

    private final DistancePolicy distanceChain;

    public SecondChain(DistancePolicy distanceChain) {
        this.distanceChain = distanceChain;
    }

    @Override
    public int calculate(int distance) {
        if (distance <= RANGE) {
            return (int) ((Math.ceil((distance - 1) / UNIT) + 1) * FARE);
        }
        return calculate(RANGE) + distanceChain.calculate(distance - RANGE);
    }
}
