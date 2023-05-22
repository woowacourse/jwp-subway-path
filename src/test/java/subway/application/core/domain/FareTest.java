package subway.application.core.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.application.core.exception.FareCantCalculatedException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FareTest {

    @Test
    @DisplayName("거리는 0KM 미만일 수 없다")
    void distance_negative_exception() {
        // given
        assertThatThrownBy(() -> Fare.of(-1))
                .isInstanceOf(FareCantCalculatedException.class);
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 5.0})
    @DisplayName("거리는 0KM 이상이라면 정상 생성된다")
    void distance_normal(double distance) {
        // given
        assertThatCode(() -> Fare.of(distance))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, 4.0, 9.0})
    @DisplayName("거리가 10KM 이내라면 기본운임이 부과된다")
    void charge_default(double distance) {
        // given
        int fare = Fare.of(distance);

        // when, then
        assertThat(fare).isEqualTo(1_250);
    }

    @ParameterizedTest
    @CsvSource(value = {"9:1250", "12:1350", "16:1450", "50:2050"}, delimiter = ':')
    @DisplayName("거리가 10KM 이상, 50KM 이하라면 5KM마다 100원이 추가된다")
    void charge_betweenTenAndFifty(double distance, int expectedValue) {
        // given
        int fare = Fare.of(distance);

        // when, then
        assertThat(fare).isEqualTo(expectedValue);
    }

    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150", "59:2250"}, delimiter = ':')
    @DisplayName("거리가 50KM보다 크다면 8KM마다 100원이 추가된다")
    void charge_overFifty(double distance, int expectedValue) {
        // given
        int fare = Fare.of(distance);

        // when, then
        assertThat(fare).isEqualTo(expectedValue);
    }
}
