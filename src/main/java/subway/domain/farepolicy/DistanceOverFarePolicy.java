package subway.domain.farepolicy;

import subway.domain.path.SubwayPath;

public class DistanceOverFarePolicy implements OverFarePolicy {

    private final int downBound;
    private final int upBound;
    private final double unitDistance;
    private final int unitOverFare;

    public DistanceOverFarePolicy(
            final int downBound,
            final int upBound,
            final double unitDistance,
            final int unitOverFare
    ) {
        this.downBound = downBound;
        this.upBound = upBound;
        this.unitDistance = unitDistance;
        this.unitOverFare = unitOverFare;
    }

    @Override
    public Fare calculateOverFare(final SubwayPath path) {
        final int overDistance = calculateOverDistance(path.getTotalDistance());
        final int overFareAmount = (int) Math.ceil(overDistance / unitDistance) * unitOverFare;
        return new Fare(overFareAmount);
    }

    private int calculateOverDistance(final int distance) {
        if (distance <= downBound) {
            return 0;
        }
        if (distance <= upBound) {
            return distance - downBound;
        }
        return upBound - downBound;
    }
}
