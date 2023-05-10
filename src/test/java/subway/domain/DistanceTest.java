package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

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
    void 거리_정보가_두_역_사이에_삽입_가능한지_확인한다(final int otherDistance, final boolean expected) {
        // given
        final Distance distance = new Distance(5);
        // when
        final boolean actual = distance.isLongerThan(otherDistance);
        // then
        assertThat(actual).isEqualTo(expected);
    }

}
