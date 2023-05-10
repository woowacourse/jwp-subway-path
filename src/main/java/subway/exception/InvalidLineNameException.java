package subway.exception;

public class InvalidLineNameException extends SubwayException {

    public InvalidLineNameException() {
        super("존재하지 않는 노선 이름입니다.");
    }
}
