package subway.acceptance.common;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import subway.common.exception.BaseExceptionType;
import subway.common.exception.ExceptionResponse;

@SuppressWarnings("NonAsciiCharacters")
public class CommonSteps {

    public static HttpStatus 정상_요청 = HttpStatus.OK;
    public static HttpStatus 정상_생성 = HttpStatus.CREATED;
    public static HttpStatus 비정상_요청 = HttpStatus.BAD_REQUEST;
    public static HttpStatus 찾을수_없음 = HttpStatus.NOT_FOUND;

    public static void 요청_결과의_상태를_검증한다(final ExtractableResponse<Response> 요청_결과, final HttpStatus 상태) {
        assertThat(요청_결과.statusCode()).isEqualTo(상태.value());
    }

    public static void 발생한_예외를_검증한다(
            final ExtractableResponse<Response> 응답,
            final BaseExceptionType 예외_타입
    ) {
        final ExceptionResponse exceptionResponse = 응답.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getCode())
                .isEqualTo(String.valueOf(예외_타입.errorCode()));
        assertThat(exceptionResponse.getMessage())
                .isEqualTo(예외_타입.errorMessage());
    }
}
