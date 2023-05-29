package subway.error.exception;

public class SectionDistanceException extends SubwayException {

	public static final SubwayException EXCEPTION = new SectionDistanceException();

	public SectionDistanceException() {
		super(new ErrorCode(400, "SECTION-400-1", "거리는 항상 양의 정수여야 합니다."));
	}
}
