package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FareTest {

    @DisplayName("10km 이내 거리만 이동했을 때의 가격을 구할 수 있다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 9, 10})
    void getFareWithin10km(int distance) {
        assertThat(Fare.getFare(distance)).isEqualTo(1250);
    }

    @DisplayName("10km 초과 50이하의 거리를 이동했을 때의 가격을 구할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"11:1350", "15:1350", "16:1450", "50:2050"}, delimiter = ':')
    void getFareWithin11kmAnd50km(int distance, int expectedFare) {
        assertThat(Fare.getFare(distance)).isEqualTo(expectedFare);
    }

    @DisplayName("10km 초과 50이하의 거리를 이동했을 때의 가격을 구할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"51:2150", "58:2150", "59:2250"}, delimiter = ':')
    void getFareOver50km(int distance, int expectedFare) {
        assertThat(Fare.getFare(distance)).isEqualTo(expectedFare);
    }
}
