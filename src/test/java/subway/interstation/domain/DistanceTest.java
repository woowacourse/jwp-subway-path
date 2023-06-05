package subway.interstation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.interstation.domain.exception.DistanceException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("거리는")
class DistanceTest {

    @Test
    void 정상적으로_생성된다() {
        final long input = 1L;

        assertThatCode(() -> new Distance(input))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "입력값: {0}")
    @ValueSource(longs = {0L, -1L})
    void 음수이면_예외가_발생한다(long input) {

        assertThatCode(() -> new Distance(input))
                .isInstanceOf(DistanceException.class)
                .hasMessage("거리는 양수이어야 합니다.");
    }

    @Test
    void 크기가_같으면_같은_객체이다() {
        Distance distance1 = new Distance(1L);
        Distance distance2 = new Distance(1L);

        assertThat(distance1).isEqualTo(distance2);
    }

    @Nested
    @DisplayName("add 메서드는")
    class Context_add {

        @Test
        void 두_거리를_더한_값을_반환한다() {
            Distance distance1 = new Distance(1L);
            Distance distance2 = new Distance(2L);

            Distance actual = distance1.add(distance2);

            assertThat(actual).isEqualTo(new Distance(3L));
        }
    }
}
