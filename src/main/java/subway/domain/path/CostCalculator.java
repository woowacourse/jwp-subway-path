package subway.domain.path;

public final class CostCalculator {

    private static final int DEFAULT_COST = 1250;
    private static final int BASE_COST_DISTANCE = 10;
    private static final int SECOND_EXTRA_COST_DISTANCE = 50;
    private static final int ADDITIONAL_COST = 100;
    private static final int DISTANCE_THRESHOLD_10KM_TO_50KM = 5;
    private static final int DISTANCE_THRESHOLD_OVER_50KM = 8;


    public static int calculateCost(int distance) {
        if (distance <= BASE_COST_DISTANCE) {
            return DEFAULT_COST;
        }
        if (distance <= SECOND_EXTRA_COST_DISTANCE) {
            distance -= BASE_COST_DISTANCE;
            return DEFAULT_COST + ((int)Math.ceil((double) distance/DISTANCE_THRESHOLD_10KM_TO_50KM) * ADDITIONAL_COST);
        }
        int cost = DEFAULT_COST + ((int)Math.ceil(((double) SECOND_EXTRA_COST_DISTANCE - BASE_COST_DISTANCE)/DISTANCE_THRESHOLD_10KM_TO_50KM) * ADDITIONAL_COST);
        distance -= SECOND_EXTRA_COST_DISTANCE;
        return cost + ((int)Math.ceil((double) distance/DISTANCE_THRESHOLD_OVER_50KM) * ADDITIONAL_COST);
    }
}
