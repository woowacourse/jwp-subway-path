package subway.error.exception;

public class LineNotFoundException extends SubwayException{

	public static final SubwayException EXCEPTION = new LineNotFoundException();

	public LineNotFoundException() {
		super(new ErrorCode(404, "LINE-404-1", "해당 노선을 찾을 수 없습니다."));
	}
}
