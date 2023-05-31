package subway.exception;

public class CannotLinkException extends SubwayException {

    public CannotLinkException() {
        super("현재 등록된 역 중에 하나를 포함해야합니다.");
    }
}
