package subway.application.charge;

public enum ChargeDistance {
    SHORT(10, 5),
    LONG(50, 8),
    ;

    private final int distance;
    private final int unit;

    ChargeDistance(int distance, int unit) {
        this.distance = distance;
        this.unit = unit;
    }

    public int getDistance() {
        return distance;
    }

    public int getUnit() {
        return unit;
    }
}
