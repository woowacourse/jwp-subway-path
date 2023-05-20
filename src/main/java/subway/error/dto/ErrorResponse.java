package subway.error.dto;

public class ErrorResponse {

	private final int status;
	private final String code;
	private final String message;

	public ErrorResponse(final int status, final String code, final String message) {
		this.status = status;
		this.code = code;
		this.message = message;
	}

	@Override
	public String toString() {
		return "{\n" +
			"\t\"status\": " + status +
			",\t\"code\": \"" + code + '\"' +
			",\n\t\"message\": \"" + message + '\"' +
			"\n}";
	}
}
