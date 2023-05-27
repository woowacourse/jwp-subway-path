package subway.domain.fare;

public enum DistanceProportionRange {
    FIRST_DISTANCE_RANGE(10, 50, 5),
    SECOND_DISTANCE_RANGE(50, 1_000_000, 8),
    ;

    private final int lowerBoundRange;
    private final int upperBoundRange;
    private final int surchargeDistanceUnit;

    DistanceProportionRange(final int lowerBoundRange, final int upperBoundRange, final int surchargeDistanceUnit) {
        this.lowerBoundRange = lowerBoundRange;
        this.upperBoundRange = upperBoundRange;
        this.surchargeDistanceUnit = surchargeDistanceUnit;
    }

    public int getLowerBoundRange() {
        return lowerBoundRange;
    }

    public int getUpperBoundRange() {
        return upperBoundRange;
    }

    public int getSurchargeDistanceUnit() {
        return surchargeDistanceUnit;
    }
}
