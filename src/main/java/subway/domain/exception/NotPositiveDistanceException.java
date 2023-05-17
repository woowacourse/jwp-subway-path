package subway.domain.exception;

public class NotPositiveDistanceException extends BusinessException {
    public NotPositiveDistanceException() {
        super("구간은 양수여야 합니다");
    }
}
