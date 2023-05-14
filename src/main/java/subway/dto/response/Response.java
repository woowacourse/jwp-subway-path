package subway.dto.response;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import subway.dto.response.ErrorResponse.ErrorResponseBuilder;
import subway.dto.response.ResultResponse.ResultResponseBuilder;

public abstract class Response {
    private final String message;

    protected Response(String message) {
        this.message = message;
    }

    public static ResponseBuilder ok() {
        return new ResponseBuilder(ResponseEntity.ok());
    }

    public static ResponseBuilder created(URI location) {
        return new ResponseBuilder(ResponseEntity.created(location));
    }

    public static ResponseBuilder badRequest() {
        return new ResponseBuilder(ResponseEntity.badRequest());
    }

    public static ResponseBuilder internalServerError() {
        return new ResponseBuilder(ResponseEntity.internalServerError());
    }

    public String getMessage() {
        return message;
    }

    protected abstract static class ResponseBuilderBase {
        protected final BodyBuilder bodyBuilder;
        protected String message;

        protected ResponseBuilderBase(BodyBuilder bodyBuilder) {
            this.bodyBuilder = bodyBuilder;
        }

        public abstract ResponseEntity<Response> build();
    }

    public static class ResponseBuilder extends ResponseBuilderBase {
        public ResponseBuilder(BodyBuilder bodyBuilder) {
            super(bodyBuilder);
        }

        public ResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public <T> ResultResponseBuilder<T> result(T result) {
            return new ResultResponseBuilder<>(bodyBuilder, message, result);
        }

        public ErrorResponseBuilder validation(MethodArgumentNotValidException e) {
            Map<String, String> validation = new HashMap<>();
            for (FieldError fieldError : e.getFieldErrors()) {
                validation.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return new ErrorResponseBuilder(bodyBuilder, message, validation);
        }

        public ErrorResponseBuilder validation(String field, String cause) {
            HashMap<String, String> validation = new HashMap<>();
            validation.put(field, cause);
            return new ErrorResponseBuilder(bodyBuilder, message, validation);
        }

        @Override
        public ResponseEntity<Response> build() {
            return bodyBuilder.body(new SimpleResponse(message));
        }
    }
}
