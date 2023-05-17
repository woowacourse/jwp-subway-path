package subway.domain.exception;

public class NoSuchStationException extends BusinessException {

    public NoSuchStationException() {
        super("호선에 없는 역입니다");
    }
}
