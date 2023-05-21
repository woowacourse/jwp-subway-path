package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    @DisplayName("길이가 1 미만이면 예외가 발생해야 한다.")
    void create_lessThan1(int distance) {
        // expect
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간의 길이는 1 이상 이어야 합니다.");
    }

    @Test
    @DisplayName("길이가 100을 초과하면 예외가 발생해야 한다.")
    void create_overThan100() {
        // given
        int distance = 101;

        // expect
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("구간의 길이는 100 이하여야 합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 99, 100})
    @DisplayName("길이가 정상적으로 생성되어야 한다.")
    void create_success(int input) {
        // when
        Distance distance = new Distance(input);

        // then
        assertThat(distance.getDistance())
                .isEqualTo(input);
    }
}
