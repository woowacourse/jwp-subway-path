package subway.domain.path;

import subway.exception.fee.InvalidFeeValue;

public class Fee {

    private final int fee;

    public Fee(int fee) {
        validateFee(fee);
        this.fee = fee;
    }

    private void validateFee(final int fee) {
        if (fee < 0) {
            throw new InvalidFeeValue();
        }
    }

    public int getFee() {
        return fee;
    }
}
