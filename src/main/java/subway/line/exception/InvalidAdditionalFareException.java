package subway.line.exception;

public class InvalidAdditionalFareException extends RuntimeException {

    public InvalidAdditionalFareException(int additionalFare) {
        super("추가 요금은 음수가 될 수 없습니다. additionalFare: " + additionalFare);
    }
}
