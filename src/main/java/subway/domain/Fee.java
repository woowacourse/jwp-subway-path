package subway.domain;

public class Fee {

    private static final int DEFAULT_FEE = 1250;
    private static final int SURCHARGE_FEE = 2050;

    private final int fee;

    private Fee(final int fee) {
        this.fee = fee;
    }

    public static Fee toDistance(int distance) {
        return new Fee(calculateFeeByDistance(distance));
    }

    private static int calculateFeeByDistance(final int distance) {
        int default_fee = DEFAULT_FEE;

        if (distance < 10) {
            return default_fee;
        }

        if (distance <= 50) {
            int excessCharge = distance / 5 - 1;
            return default_fee + excessCharge * 100;
        }

        return SURCHARGE_FEE + ((distance - 50) / 8 + 1) * 100;
    }

    public int getFee() {
        return fee;
    }

}
