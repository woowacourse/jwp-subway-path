package subway.exception.notfound;

public class LineNotFoundException extends NotFoundException {

    public LineNotFoundException() {
        super("노선 정보를 찾을 수 없습니다.");
    }
}
