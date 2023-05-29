package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.section.Distance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DistanceTest {

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 거리는_음수가_될_수_없다(final int value) {
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("거리는 양수여야 합니다.");
    }

    @ParameterizedTest
    @CsvSource({"4, true", "5, false", "6, false"})
    void 거리가_더_길다(final int otherDistance, final boolean expected) {
        // given
        final Distance distance = new Distance(5);

        // when
        final boolean actual = distance.isLongerThan(otherDistance);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"4, false", "5, true", "6, true"})
    void 거리가_더_길지_않다(final int otherDistance, final boolean expected){
        // given
        final Distance distance = new Distance(5);

        // when
        final boolean actual = distance.isNotLongerThan(otherDistance);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 거리를_뺀다(){
        //given
        final Distance distance = new Distance(5);

        //when
        final Distance subtractedDistance = distance.subtract(3);

        //then
        assertThat(subtractedDistance.getDistance()).isEqualTo(2);
    }

}
