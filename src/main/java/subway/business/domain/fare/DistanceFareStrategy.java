package subway.business.domain.fare;

public class DistanceFareStrategy {
    public static final int FARE_PER_RISE_STANDARD = 100;
    private final int riseStandardKm;
    private final int upperLimitToApply;
    private final int lowerLimitToApply;

    public DistanceFareStrategy(int riseStandardKm, int upperLimitToApply, int lowerLimitToApply) {
        this.riseStandardKm = riseStandardKm;
        this.upperLimitToApply = upperLimitToApply;
        this.lowerLimitToApply = lowerLimitToApply;
    }

    public DistanceFareStrategy(int riseStandardKm, int lowerLimitToApply) {
        this(riseStandardKm, 1_000_000, lowerLimitToApply);
    }

    public int calculateFare(int distance) {
        int distanceForFare = getDistanceForFare(distance);
        if (distanceForFare <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((distanceForFare - 1) / riseStandardKm) + 1) * FARE_PER_RISE_STANDARD);
    }

    private int getDistanceForFare(int distance) {
        int distanceForFare = distance;
        if (distance > upperLimitToApply) {
            distanceForFare = upperLimitToApply;
        }
        return distanceForFare - lowerLimitToApply;
    }
}
