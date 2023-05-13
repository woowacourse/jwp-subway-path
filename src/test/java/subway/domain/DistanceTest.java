package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DistanceTest {

    @ValueSource(ints = {1, 100})
    @ParameterizedTest
    void 허용된_길이가_들어오면_거리를_생성한다(final int input) {
        assertDoesNotThrow(() -> Distance.from(input));
    }

    @ValueSource(ints = {0, 101})
    @ParameterizedTest
    void 허용되지_않은_길이가_들어오면_예외가_발생한다(final int input) {

        assertThatThrownBy(() -> Distance.from(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("길이는 1 이상, 100 이하만 가능합니다.");
    }

    @Test
    void 기존_거리에_더할_거리가_들어오면_거리를_합한_값을_반환한다() {
        // given
        final Distance first = Distance.from(1);
        final Distance second = Distance.from(2);

        // when
        final Distance actual = first.add(second);

        // then
        assertThat(actual.getDistance()).isEqualTo(3);
    }

    @Test
    void 기존_거리에_빼야할_거리가_들어오면_거리를_뺀_값을_반환한다() {
        // given
        final Distance first = Distance.from(2);
        final Distance second = Distance.from(1);

        // when
        final Distance actual = first.minus(second);

        // then
        assertThat(actual.getDistance()).isEqualTo(1);
    }
}
