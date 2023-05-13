package subway.domain.interstation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.interstation.DistanceException;

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
    void 음수이면_예외가_발생한다(final long input) {

        assertThatCode(() -> new Distance(input))
            .isInstanceOf(DistanceException.class)
            .hasMessage("거리는 양수이어야 합니다.");
    }

    @Test
    void 크기가_같으면_같은_객체이다() {
        final Distance distance1 = new Distance(1L);
        final Distance distance2 = new Distance(1L);

        assertThat(distance1).isEqualTo(distance2);
    }
}
