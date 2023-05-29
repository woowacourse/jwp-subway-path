package subway.error.exception;

public class SectionConnectionException extends SubwayException {
	public SectionConnectionException(final String message) {
		super(new ErrorCode(400, "SECTION-400-1", message));
	}
}
