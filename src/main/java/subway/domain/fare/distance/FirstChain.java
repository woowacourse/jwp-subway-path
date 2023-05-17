package subway.domain.fare.distance;

import subway.domain.fare.DistancePolicy;

public class FirstChain implements DistancePolicy {
    private static final int BASIC_FARE = 1250;
    private static final int RANGE = 10;

    private final DistancePolicy distanceChain;

    public FirstChain(DistancePolicy distanceChain) {
        this.distanceChain = distanceChain;
    }

    @Override
    public int calculate(int distance) {
        if (distance <= RANGE) {
            return BASIC_FARE;
        }

        return BASIC_FARE + distanceChain.calculate(distance - RANGE);
    }

}
