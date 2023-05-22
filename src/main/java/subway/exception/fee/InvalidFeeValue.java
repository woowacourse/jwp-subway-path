package subway.exception.fee;

public class InvalidFeeValue extends FeeException {

    private final static String INVALID_FEE_MESSAGE = "올바르지 않은 요금입니다.";

    public InvalidFeeValue() {
        super(INVALID_FEE_MESSAGE);
    }
}
