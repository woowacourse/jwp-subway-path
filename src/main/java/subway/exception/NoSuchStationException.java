package subway.exception;

public class NoSuchStationException extends RuntimeException{
    public NoSuchStationException() {
        super("해당하는 역이 없습니다.");
    }
}
