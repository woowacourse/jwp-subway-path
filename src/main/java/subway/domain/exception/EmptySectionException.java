package subway.domain.exception;

public class EmptySectionException extends BusinessException {

    public EmptySectionException() {
        super("구간은 두 역이 있어야 합니다");
    }
}
