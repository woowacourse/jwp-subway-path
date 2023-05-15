package subway.exception;

public class InvalidDistanceException extends GlobalException {

    private static final String messageFormat = "추가하려는 구간의 길이가 기존 구간의 길이보다 깁니다. 입력값 : %d, 기존 구간의 길이 : %d";

    public InvalidDistanceException(final Integer wrongDistance, final Integer existDistance) {
        super(String.format(messageFormat, wrongDistance, existDistance));
    }
}
