package subway.exception.line;

public class LineIsInitException extends LineException {

    private static final String LINE_IS_INIT_EXCEPTION = "역 하나 등록 시에는 노선에 적어도 구간이 하나 있어야 합니다.";

    public LineIsInitException() {
        super(LINE_IS_INIT_EXCEPTION);
    }
}
