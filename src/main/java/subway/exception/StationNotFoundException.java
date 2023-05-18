package subway.exception;

public class StationNotFoundException extends GlobalException {

    private static final String messageFormat = "이미 역이 존재합니다. 입력값 : %d";

    public StationNotFoundException(final Long wrongStationId) {
        super(String.format(messageFormat, wrongStationId));
    }
}
