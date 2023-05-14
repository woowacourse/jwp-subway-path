package subway.exception;

public class LineNumberUnderMinimumNumber extends RuntimeException {

    public LineNumberUnderMinimumNumber() {
        super("LineNumber은 최소 0이상이 되어야합니다.");
    }
}
