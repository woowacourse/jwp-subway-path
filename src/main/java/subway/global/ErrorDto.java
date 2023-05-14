package subway.global;

public class ErrorDto {

    private final String message;
    private final String requestURI;
    private final int httpStatusCode;

    public ErrorDto(String message, String requestURI, int httpStatusCode) {
        this.message = message;
        this.requestURI = requestURI;
        this.httpStatusCode = httpStatusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
