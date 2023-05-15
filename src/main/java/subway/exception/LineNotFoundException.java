package subway.exception;

public class LineNotFoundException extends GlobalException{

    private static final String messageFormat = "존재하지 않는 노선입니다. 입력값 : %d";

    public LineNotFoundException(final Long wrongId) {
        super(String.format(messageFormat, wrongId));
    }
}
