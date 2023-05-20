package subway.domain.path;

public final class CostCalculator {

    private static final int DEFAULT_COST = 1250;
    private static final int SECTION_1 = 10;
    private static final int SECTION_2 = 50;
    private static final int ADDITIONAL_COST = 100;
    private static final int DISTANCE_THRESHOLD_10KM_TO_50KM = 5;
    private static final int DISTANCE_THRESHOLD_OVER_50KM = 8;


    public static int calculateCost(int distance) {
        if (distance < SECTION_1) {
            return DEFAULT_COST;
        }
        if (distance == SECTION_1) {
            return DEFAULT_COST + ADDITIONAL_COST;
        }
        if (distance <= SECTION_2) {
            distance -= SECTION_1;
            return DEFAULT_COST + ((int)Math.ceil((double) distance/DISTANCE_THRESHOLD_10KM_TO_50KM) * ADDITIONAL_COST);
        }
        int cost = DEFAULT_COST + ((int)Math.ceil(((double)SECTION_2-SECTION_1)/DISTANCE_THRESHOLD_10KM_TO_50KM) * ADDITIONAL_COST);
        distance -= SECTION_2;
        return cost + ((int)Math.ceil((double) distance/DISTANCE_THRESHOLD_OVER_50KM) * ADDITIONAL_COST);
    }
}
