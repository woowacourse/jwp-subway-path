package subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class SubwayFareTest {

    @ParameterizedTest
    @CsvSource(value = {"1:1250", "10:1250", "11:1350", "15:1350", "16:1450", "20:1450", "46:2050", "50:2050", "51:2150", "58:2150"}, delimiter = ':')
    @DisplayName("거리에 따라서 계산된 요금을 생성한다.")
    void generateFareByDistance(int distance, int expectedFare) {
        // when
        SubwayFare subwayFare = SubwayFare.generateFareByDistance(distance);

        // then
        assertThat(subwayFare.getFare()).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("요금 생성 시 요금을 계산할 수 없는 거리면 예외가 발생한다.")
    void generateFareByDistance_throw_not_calculable_distance(int distance) {
        // when, then
        assertThatThrownBy(() -> SubwayFare.generateFareByDistance(distance))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("요금을 계산할 수 없는 거리입니다.");
    }
}
