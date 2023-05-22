package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.domain.Fare.calculate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FareTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @DisplayName("거리가 10 이하일 때는 기본 운임(1,250원)만 부과한다.")
    void calculateWhenDistanceWithinTen(final int distance) {
        assertThat(calculate(distance)).isEqualTo(1250);
    }

    @ParameterizedTest
    @CsvSource(value = {"11:1350", "15:1350", "16:1450", "32:1750", "48:2050", "50:2050"}, delimiter = ':')
    @DisplayName("거리가 10 초과, 50 이하일 때는 기본 운임(1,250원)에 5km 까지 마다 100원 추가한다.")
    void calculateWhenDistanceWithinFifty(final int distance, final int expected) {
        assertThat(calculate(distance)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150", "60:2250", "70:2350"}, delimiter = ':')
    @DisplayName("거리가 50 초과일 때는 기본 운임(1,250원)에 8km 까지 마다 100원 추가한다.")
    void calculateWhenDistanceOverFifty(final int distance, final int expected) {
        assertThat(calculate(distance)).isEqualTo(expected);
    }
}
