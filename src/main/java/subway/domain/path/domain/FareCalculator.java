package subway.domain.path.domain;

public enum FareCalculator {

    BETWEEN_10KM_50KM(10, 50, 5, 100),
    OVER_50KM(50, Integer.MAX_VALUE, 8, 100);

    private static final int DEFAULT_FARE = 1_250;

    private final int minDistance;
    private final int maxDistance;
    private final int additionalDistance;
    private final int additionalFare;

    FareCalculator(final int minDistance, final int maxDistance, final int additionalDistance, final int additionalFare) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.additionalDistance = additionalDistance;
        this.additionalFare = additionalFare;
    }

    public static int calculate(double distance) {
        int additionalFare = 0;
        for (FareCalculator value : FareCalculator.values()) {
            if (value.maxDistance < distance) {
                additionalFare += (Math.ceil((value.maxDistance - value.minDistance) / value.additionalDistance)) * value.additionalFare;
            }
            if (value.minDistance < distance && distance < value.maxDistance) {
                additionalFare += (Math.ceil((distance - value.minDistance) / value.additionalDistance)) * value.additionalFare;
            }
        }
        return DEFAULT_FARE + additionalFare;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public int getAdditionalDistance() {
        return additionalDistance;
    }

    public int getAdditionalFare() {
        return additionalFare;
    }
}
