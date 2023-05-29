package subway.error.exception;

public class StationNotFoundException extends SubwayException {

	public static final SubwayException EXCEPTION = new StationNotFoundException();

	public StationNotFoundException() {
		super(new ErrorCode(404, "STATION-404-1", "해당 역을 찾을 수 없습니다."));
	}
}
