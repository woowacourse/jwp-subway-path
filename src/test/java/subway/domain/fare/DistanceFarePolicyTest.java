package subway.domain.fare;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.line.Line;
import subway.domain.line.Lines;

import java.util.List;

@DisplayName("distanceFarePolicy 기능 테스트")
class DistanceFarePolicyTest {

    private final Line _1호선 = Line.of(1L, "1호선", "남색", 0);
    private final Lines lines = Lines.from(List.of(_1호선));

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10})
    @DisplayName("기본 운임 이내 요금 테스트")
    void calculateDefaultFareTest(int distance) {
        // given
        FarePolicy farePolicy = DistanceFarePolicy.getInstance();

        // when
        int fare = farePolicy.calculateFare(distance, lines);

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @ParameterizedTest
    @CsvSource(value = {"11, 1350", "15, 1350", "16, 1450", "49, 2050", "50, 2050"})
    @DisplayName("10~50km 운임 요금 테스트")
    void calculateFareBetween10To50Test(int distance, int expected) {
        // given
        FarePolicy farePolicy = DistanceFarePolicy.getInstance();

        // when
        int fare = farePolicy.calculateFare(distance, lines);

        // then
        assertThat(fare).isEqualTo(expected);
    }


    @ParameterizedTest
    @CsvSource(value = {"51, 2150", "58, 2150", "59, 2250"})
    @DisplayName("50km 초과 운임 요금 테스트")
    void calculateFareExceed50Test(int distance, int expected) {
        // given
        FarePolicy farePolicy = DistanceFarePolicy.getInstance();

        // when
        int fare = farePolicy.calculateFare(distance, lines);

        // then
        assertThat(fare).isEqualTo(expected);
    }

    @Test
    @DisplayName("주어진 거리가 음수일 경우 예외처리한다.")
    void validateNegativeDistanceTest() {
        // given
        FarePolicy farePolicy = DistanceFarePolicy.getInstance();

        // then
        assertThatThrownBy(() -> farePolicy.calculateFare(-1, lines))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 요금 계산 상 거리는 음수가 될 수 없습니다.");
    }
}
