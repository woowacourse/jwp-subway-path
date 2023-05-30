package subway.domain.fare;

public enum DistanceFareRate {

    BASE(10, 5, 100),
    EXCESS(50, 8, 100);

    private final int standardDistance;
    private final int interval;
    private final int farePerInterval;

    DistanceFareRate(final int standardDistance, final int interval, final int farePerInterval) {
        this.standardDistance = standardDistance;
        this.interval = interval;
        this.farePerInterval = farePerInterval;
    }

    public int standardDistance() {
        return standardDistance;
    }

    public int interval() {
        return interval;
    }

    public int farePerInterval() {
        return farePerInterval;
    }
}
