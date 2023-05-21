package subway.domain;

public class FeeCalculator {

    private static final int DEFAULT_FEE = 1250;
    private static final int EXTRA_FEE = 100;
    private static final int SECTION_ONE_CRITERIA_DISTANCE = 10;
    private static final int SECTION_ONE_UNIT = 5;
    private static final int SECTION_TWO_CRITERIA_DISTANCE = 50;
    private static final int SECTION_TWO_UNIT = 8;
    private static final int SECTION_TWO_EXTRA_FEE = 900;

    public int calculate(final double distance) {
        int totalFee = DEFAULT_FEE;

        if (is10To50Km(distance)) {
            totalFee += calculateExtraFee(distance, SECTION_ONE_CRITERIA_DISTANCE, SECTION_ONE_UNIT, EXTRA_FEE);
        }
        if (distance > SECTION_TWO_CRITERIA_DISTANCE) {
            totalFee += calculateExtraFee(distance, SECTION_TWO_CRITERIA_DISTANCE, SECTION_TWO_UNIT, SECTION_TWO_EXTRA_FEE);
        }

        return totalFee;
    }

    private boolean is10To50Km(final double distance) {
        return distance > SECTION_ONE_CRITERIA_DISTANCE && distance <= SECTION_TWO_CRITERIA_DISTANCE;
    }

    private int calculateExtraFee(double distance, double criteriaDistance, int unit, int defaultExtraFee) {
        int extraDistance = (int) Math.ceil(distance - criteriaDistance);
        if (extraDistance % unit == 0) {
            extraDistance--;
        }

        return defaultExtraFee + ((extraDistance / unit) * EXTRA_FEE);
    }
}