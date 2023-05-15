package subway.acceptance.common;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.common.exception.BaseExceptionType;
import subway.common.exception.ExceptionResponse;

@SuppressWarnings("NonAsciiCharacters")
public class CommonSteps {

    public static void 발생한_예외를_검증한다(
            final ExtractableResponse<Response> response,
            final BaseExceptionType baseExceptionType
    ) {
        final ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);
        assertThat(exceptionResponse.getCode())
                .isEqualTo(String.valueOf(baseExceptionType.errorCode()));
        assertThat(exceptionResponse.getMessage())
                .isEqualTo(baseExceptionType.errorMessage());
    }
}
