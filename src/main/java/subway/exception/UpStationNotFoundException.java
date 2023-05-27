package subway.exception;

public class UpStationNotFoundException extends RuntimeException {

	public UpStationNotFoundException() {
		super("상행 종점을 찾을 수 없습니다");
	}
}
