package subway.exception.subway;

public class StationIsNotRegisteredOnLine extends SubwayException {
    private static final String MESSAGE = "노선에 등록되지 않은 역입니다.";

    public StationIsNotRegisteredOnLine() {
        super(MESSAGE);
    }
}
