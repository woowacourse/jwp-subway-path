package subway.exception;

// TODO: 2023/05/09 예외 클래스명 수정
public class GlobalException extends RuntimeException {

    public GlobalException(final String message) {
        super(message);
    }
}
