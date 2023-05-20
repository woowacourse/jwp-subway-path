package subway.global.common;

import org.springframework.http.HttpStatus;

public class ResultResponse {

    private int status;
    private String message;
    private Object data;

    public ResultResponse() {
    }

    private ResultResponse(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static ResultResponse of(HttpStatus httpStatus, Object data) {
        return new ResultResponse(httpStatus.value(), httpStatus.getReasonPhrase(), data);
    }

    public static ResultResponse of(HttpStatus httpStatus) {
        return new ResultResponse(httpStatus.value(), httpStatus.getReasonPhrase(), null);
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
