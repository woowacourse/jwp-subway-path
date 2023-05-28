package subway.exception;

public class DuplicateLineException extends SubwayException {

    public DuplicateLineException() {
        super("이미 존재하는 노선입니다.");
    }
}
