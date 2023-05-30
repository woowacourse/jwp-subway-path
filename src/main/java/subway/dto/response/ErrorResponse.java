package subway.dto.response;

import java.util.HashMap;
import java.util.Map;

public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    private ErrorResponse(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse badRequest(String message) {
        return new ErrorResponse("400", message);
    }

    public void addValidation(String fieldName, String errorMessage) {
        validation.put(fieldName, errorMessage);
    }
}
