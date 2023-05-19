package subway.exception;

public class InValidStationNameException extends GlobalException {
    public InValidStationNameException() {
        super("역 이름은 1글자 이상, 10글자 이하여야 합니다.");
    }
}
