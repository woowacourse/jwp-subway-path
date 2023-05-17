package subway.domain.fee;

public class NormalFeeStrategy implements FeeStrategy {

    private static final int BASE_FEE = 1250;
    private static final int BASE_FEE_DISTANCE = 10;
    private static final int ADDITIONAL_FEE_UNIT_CHANGE_DISTANCE = 50;
    private static final int ADDITIONAL_FEE_FIRST_UNIT = 5;
    private static final int ADDITIONAL_FEE_SECOND_UNIT = 8;
    private static final int ADDITIONAL_FEE_PER_UNIT = 100;

    @Override
    public int calculate(int distance) {
        if (distance <= BASE_FEE_DISTANCE) {
            return BASE_FEE;
        }
        if (distance <= ADDITIONAL_FEE_UNIT_CHANGE_DISTANCE) {
            return BASE_FEE + ((int)(Math.ceil((double)(distance - BASE_FEE_DISTANCE) / ADDITIONAL_FEE_FIRST_UNIT))
                * ADDITIONAL_FEE_PER_UNIT);
        }
        return
            (BASE_FEE +
                ((ADDITIONAL_FEE_UNIT_CHANGE_DISTANCE - BASE_FEE_DISTANCE) / ADDITIONAL_FEE_FIRST_UNIT)
                    * ADDITIONAL_FEE_PER_UNIT +
                ((int)(Math.ceil((double)(distance - ADDITIONAL_FEE_UNIT_CHANGE_DISTANCE) / ADDITIONAL_FEE_SECOND_UNIT))
                    * ADDITIONAL_FEE_PER_UNIT));
    }
}
