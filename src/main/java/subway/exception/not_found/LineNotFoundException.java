package subway.exception.not_found;

public class LineNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 노선입니다.";

    public LineNotFoundException() {
        super(MESSAGE);
    }

}
