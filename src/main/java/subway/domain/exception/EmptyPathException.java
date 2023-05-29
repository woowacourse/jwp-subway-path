package subway.domain.exception;

public class EmptyPathException extends BusinessException {
    public EmptyPathException() {
        super("경로가 없습니다");
    }
}
