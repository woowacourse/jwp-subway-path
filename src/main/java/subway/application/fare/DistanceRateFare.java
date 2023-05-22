package subway.application.fare;

import subway.domain.Distance;
import subway.domain.Fare;


public abstract class DistanceRateFare {

    public static final Distance DEFAULT_DISTANCE = Distance.from(10);
    public static final Distance DISTANCE_50 = Distance.from(50);

    private final int perDistance;
    private final int perFare;

    public DistanceRateFare(int perDistance, int perFare) {
        this.perDistance = perDistance;
        this.perFare = perFare;
    }

    protected final Fare calculate(Distance distance) {
        final double overDistance = Math.ceil((distance.getDistance() - 1) / perDistance) + 1;
        return Fare.from((int) (overDistance * perFare));
    }

    public abstract Fare calculateFare(Distance distance);

    abstract boolean support(Distance distance);
}
