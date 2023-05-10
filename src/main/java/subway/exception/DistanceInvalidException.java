package subway.exception;

public class DistanceInvalidException extends RuntimeException {

    public DistanceInvalidException() {
        super("갈랫길이 생기면 안됩니다. 길이를 다시 입력해주세요.");
    }
}
