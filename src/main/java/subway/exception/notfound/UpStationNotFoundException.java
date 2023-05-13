package subway.exception.notfound;

public class UpStationNotFoundException extends NotFoundException {

    public UpStationNotFoundException() {
        super("상행 종점을 찾을 수 없습니다.");
    }
}
