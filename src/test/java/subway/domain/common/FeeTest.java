package subway.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import subway.exception.InvalidDistanceException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class FeeTest {

    @ParameterizedTest
    @CsvSource(value = {"9, 1250", "12, 1350", "16, 1450", "50, 2050", "57, 2150", "58, 2150"})
    @DisplayName("거리 기준으로 요금을 계산한다.")
    void calculate_fee_from_distance_success(final int distance, final int expected) {
        // given
        Fee fee = Fee.createDefault();

        // when
        fee.calculateFromDistance(distance);

        // then
        assertThat(fee.getFee()).isEqualTo(expected);
    }

    @Test
    @DisplayName("역 사이의 거리가 1보다 작으면 예외를 발생시킨다.")
    void throws_exception_when_move_distance_invalid() {
        // when & then
        assertThatThrownBy(() -> Fee.createDefault().calculateFromDistance(0))
                .isInstanceOf(InvalidDistanceException.class);
    }
}
