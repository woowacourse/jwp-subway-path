package subway.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.service.section.domain.Distance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SuppressWarnings("NonAsciiCharacters")
class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 역_간_거리가_양의정수가_아니면_예외(int distance) {
        assertThatThrownBy(() -> new Distance(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지하철 역 간 거리는 양의 정수만 가능합니다.");
    }

    @Test
    void 역_간_거리가_양의정수면_정상수행() {
        assertDoesNotThrow(() -> new Distance(10));
    }

    @Test
    void 입력으로_들어온_거리를_뺀_새로운_거리_정보를_반환한다() {
        Distance original = new Distance(10);
        Distance minusDistance = new Distance(3);

        Distance reducedDistance = original.reduce(minusDistance);

        assertThat(reducedDistance).isEqualTo(new Distance(7));
    }

    @ParameterizedTest
    @ValueSource(ints = {14, 10})
    void 입력으로_들어온_거리보다_작으면_true(int distance) {
        Distance original = new Distance(10);
        assertThat(original.isSmaller(new Distance(distance))).isTrue();
    }


    @ParameterizedTest
    @ValueSource(ints = {1, 9})
    void 입력으로_들어온_거리보다_크면_false(int distance) {
        Distance original = new Distance(10);
        assertThat(original.isSmaller(new Distance(distance))).isFalse();
    }

    @Test
    void 두_개의_거리를_합해_새로운_객체를_반환() {
        Distance firstDistance = new Distance(3);
        Distance secondDistance = new Distance(5);

        assertThat(firstDistance.plus(secondDistance)).isEqualTo(new Distance(8));
    }
}
