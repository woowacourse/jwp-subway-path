package subway.presentation.dto.response;

public class BadResponse {
    private final String errorMessage;

    public BadResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
