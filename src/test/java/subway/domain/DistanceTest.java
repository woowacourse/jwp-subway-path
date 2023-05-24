package subway.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class DistanceTest {

    @Test
    void zero를_호출하면_거리가_0인_값이_반환된다() {
        // when
        final Distance zero = Distance.zero();

        // then
        assertThat(zero).isEqualTo(Distance.from(0));
    }

    @Test
    void plus() {
        // given
        final Distance ten = Distance.from(10);
        final Distance seven = Distance.from(7);

        // when
        final Distance result = ten.plus(seven);

        // then
        assertThat(result).isEqualTo(Distance.from(17));
    }

    @Test
    void minus() {
        // given
        final Distance ten = Distance.from(10);
        final Distance seven = Distance.from(7);

        // when
        final Distance result = ten.minus(seven);

        // then
        assertThat(result).isEqualTo(Distance.from(3));
    }

    @Test
    void minus를_했을_때_음수가_나오면_실패한다() {
        // given
        final Distance seven = Distance.from(7);
        final Distance ten = Distance.from(10);

        // when, then
        assertThatThrownBy(() -> seven.minus(ten))
                .isInstanceOf(IllegalStateException.class);
    }
}
