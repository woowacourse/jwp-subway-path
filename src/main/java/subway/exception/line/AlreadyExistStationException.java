package subway.exception.line;

public class AlreadyExistStationException extends LineException {

    private final static String ALREADY_EIXST_STATION_MESSAGE = "이미 존재하는 역입니다.";

    public AlreadyExistStationException() {
        super(ALREADY_EIXST_STATION_MESSAGE);
    }
}
