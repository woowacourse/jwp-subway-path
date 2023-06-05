package subway.common.exception;

public class ErrorResponse<T> {

    private final T data;

    public ErrorResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
}
