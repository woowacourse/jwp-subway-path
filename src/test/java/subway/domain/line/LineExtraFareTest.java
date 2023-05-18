package subway.domain.line;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.exception.ErrorCode.LINE_EXTRA_FARE_RANGE;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.BadRequestException;

class LineExtraFareTest {

    @ParameterizedTest(name = "0 이상의 추가 요금은 정상적으로 생성된다.")
    @ValueSource(ints = {0, 1, 2})
    void line_extra_fare_success(final int extraFare) {
        final LineExtraFare lineExtraFare = assertDoesNotThrow(() -> new LineExtraFare(extraFare));
        assertThat(lineExtraFare)
            .extracting(LineExtraFare::fare)
            .isEqualTo(extraFare);
    }

    @ParameterizedTest(name = "0 미만의 추가 요금은 예외가 발생한다..")
    @ValueSource(ints = {-1, -2})
    void line_extra_fare_fail(final int extraFare) {
        assertThatThrownBy(() -> new LineExtraFare(extraFare))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(LINE_EXTRA_FARE_RANGE);
    }
}
