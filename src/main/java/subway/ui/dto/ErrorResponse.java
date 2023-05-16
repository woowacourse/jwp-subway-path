package subway.ui.dto;

public class ErrorResponse {

    private final String message;

    public ErrorResponse(final String message) {
        this.message = message;
    }

    public ErrorResponse(final String message, final String field) {
        this.message = String.format("%s (field: %s)", field);
    }

    public String getMessage() {
        return message;
    }
}
