package subway.dto.response;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

public class ErrorResponse extends Response {
    private final Map<String, String> validation;

    public ErrorResponse(String message, Map<String, String> validation) {
        super(message);
        this.validation = validation;
    }

    public Map<String, String> getValidation() {
        return validation;
    }

    public static class ErrorResponseBuilder extends ResponseBuilderBase {
        private final Map<String, String> validation;

        public ErrorResponseBuilder(BodyBuilder bodyBuilder, String message, Map<String, String> validation) {
            super(bodyBuilder);
            this.message = message;
            this.validation = validation;
        }

        public ErrorResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ErrorResponseBuilder validation(String field, String cause) {
            validation.put(field, cause);
            return this;
        }

        @Override
        public ResponseEntity<Response> build() {
            return bodyBuilder.body(new ErrorResponse(message, validation));
        }
    }
}
