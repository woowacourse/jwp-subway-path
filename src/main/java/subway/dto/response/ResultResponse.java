package subway.dto.response;

import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;

public class ResultResponse<T> extends Response {
    private final T result;

    public ResultResponse(String message, T result) {
        super(message);
        this.result = result;
    }

    public T getResult() {
        return result;
    }

    public static class ResultResponseBuilder<T> extends ResponseBuilderBase {
        private final T result;

        public ResultResponseBuilder(BodyBuilder bodyBuilder, String message, T result) {
            super(bodyBuilder);
            this.message = message;
            this.result = result;
        }

        public ResultResponseBuilder<T> message(String message) {
            this.message = message;
            return this;
        }

        @Override
        public ResponseEntity<Response> build() {
            return bodyBuilder.body(new ResultResponse<>(message, result));
        }
    }
}
