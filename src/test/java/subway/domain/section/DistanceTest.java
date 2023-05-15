package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DistanceTest {

    @Test
    @DisplayName("역 사이의 거리가 100km 초과면 예외가 발생한다.")
    void validateDistanceTest() {
        // given
        int invalidDistance = 101;

        // when, then
        assertThatThrownBy(() -> new Distance(invalidDistance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 사이 거리는 100km 이하여야 합니다.");
    }

    @Test
    @DisplayName("현재 거리에서 대상 거리를 뺀 거리를 반환한다.")
    void subtract() {
        // given
        Distance currentDistance = new Distance(10);
        Distance targetDistance = new Distance(4);

        // when
        Distance subtractDistance = currentDistance.subtract(targetDistance);

        // then
        assertThat(subtractDistance.getDistance()).isEqualTo(6);
    }

    @ParameterizedTest
    @CsvSource(value = {"10:5:True", "10:15:False"}, delimiter = ':')
    @DisplayName("현재 거리가 대상 거리보다 크거나 같으면 True, 작으면 False를 반환한다.")
    void isGreaterThanOrEqual(int currentDistance, int targetDistance, boolean expected) {
        // given
        Distance generatedCurrentDistance = new Distance(currentDistance);
        Distance generatedTargetDistance = new Distance(targetDistance);

        // when, then
        assertThat(generatedCurrentDistance.isGreaterThanOrEqual(generatedTargetDistance)).isEqualTo(expected);
    }
}
