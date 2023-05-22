package subway.domain;

public class SubwayFareManager {
    private static final int BASIC_DISTANCE_LIMIT = 10;
    private static final int ONE_STEP_DISTANCE_LIMIT = 50;
    private static final double ONE_STEP_DISTANCE_UNIT = 5;
    private static final double TWO_STEP_DISTANCE_UNIT = 8;
    private static final int BASIC_CHARGE = 1250;
    private static final int ADDITIONAL_CHARGE = 100;

    public Integer calculateChargeForDistance(final Integer totalDistance) {
        if (totalDistance > ONE_STEP_DISTANCE_LIMIT) {
            int oneStepAdditional = (int) ((ONE_STEP_DISTANCE_LIMIT - BASIC_CHARGE) / ONE_STEP_DISTANCE_UNIT);
            int twoStepAdditional = (int) Math.ceil((totalDistance - ONE_STEP_DISTANCE_LIMIT)/ TWO_STEP_DISTANCE_UNIT);
            return BASIC_CHARGE
                    + oneStepAdditional * ADDITIONAL_CHARGE
                    + twoStepAdditional * ADDITIONAL_CHARGE;
        }
        if (totalDistance > BASIC_DISTANCE_LIMIT) {
            int oneStepAdditional = (int) Math.ceil((totalDistance - BASIC_DISTANCE_LIMIT)/ ONE_STEP_DISTANCE_UNIT);
            return BASIC_CHARGE
                    + oneStepAdditional * ADDITIONAL_CHARGE;
        }
        return BASIC_CHARGE;
    }
}
