package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FareTest {

    @DisplayName("거리가 10이하면 기본 요금을 적용한다.")
    @Test
    void calculateFare() {
        // given
        int distance = 10;
        Fare fare = Fare.from(distance);

        // when, then
        assertThat(fare.getValue()).isEqualTo(1_250);
    }

    @DisplayName("거리가 10이상일 때 50km까지 5km당 100원씩 추가로 적용한다.")
    @ParameterizedTest
    @CsvSource(value = {"11:1_350", "16:1_450", "21:1_550", "26:1_650", "31:1_750", "36:1_850", "41:1_950", "46:2_050"},
            delimiter = ':')
    void calculateOverFare(int distance, int expectedFare) {
        // given
        Fare fare = Fare.from(distance);

        // when, then
        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }

    @DisplayName("거리가 50초과일 때 8km당 100원씩 추가로 적용한다.")
    @ParameterizedTest
    @CsvSource(value = {"51:2_150", "58:2_150", "59:2_250", "66:2_250", "67:2_350", "74:2_350"},
            delimiter = ':')
    void calculateOverFareWhenOver50(int distance, int expectedFare) {
        // given
        Fare fare = Fare.from(distance);

        // when, then
        assertThat(fare.getValue()).isEqualTo(expectedFare);
    }
}
