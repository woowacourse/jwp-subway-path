package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.exception.IllegalDistanceArgumentException;
import subway.domain.vo.Distance;

@DisplayName("거리 단위 테스트")
class DistanceTest {

    @DisplayName("거리는 0 또는 음의 정수일 수 없다")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void createFail(int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalDistanceArgumentException.class)
                .hasMessageContaining("거리는 양의 정수여야 합니다.");
    }

    @DisplayName("거리에서 뺀 값만큼의 거리를 반환한다")
    @Test
    void minus() {
        Distance from = new Distance(2);
        Distance using = new Distance(1);

        Distance result = from.minus(using);

        Assertions.assertThat(result.getValue())
                .isEqualTo(1);
    }

    @DisplayName("거리에서 뺀 값만큼의 거리를 반환한다")
    @Test
    void plus() {
        Distance from = new Distance(2);
        Distance using = new Distance(1);

        Distance result = from.plus(using);

        Assertions.assertThat(result.getValue())
                .isEqualTo(3);
    }
}
