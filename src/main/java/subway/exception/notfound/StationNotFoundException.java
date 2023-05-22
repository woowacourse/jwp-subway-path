package subway.exception.notfound;

public class StationNotFoundException extends NotFoundException {

    public StationNotFoundException() {
        super("역 정보를 찾을 수 없습니다.");
    }
}
