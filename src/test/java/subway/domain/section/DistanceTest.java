package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class DistanceTest {

    @ParameterizedTest(name = "길이로 {0}가 전달하면 예외가 발생한다.")
    @ValueSource(ints = {0, 101})
    void from_메소드는_1부터_100_사이가_아닌_길이를_전달하면_예외가_발생한다(final int invalidDistance) {

        assertThatThrownBy(() -> Distance.from(invalidDistance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("길이는 1 이상, 100 이하만 가능합니다.");
    }

    @Test
    void plus_메소드는_기존_거리에_더할_거리를_전달하면_합을_반환한다() {
        final Distance first = Distance.from(1);
        final Distance second = Distance.from(2);

        final Distance actual = first.plus(second);

        assertThat(actual.getDistance()).isEqualTo(3);
    }

    @Test
    void minus_메소드는_기존_거리에_뺄_거리를_전달하면_차이를_반환한다() {
        final Distance first = Distance.from(2);
        final Distance second = Distance.from(1);

        final Distance actual = first.minus(second);

        assertThat(actual.getDistance()).isEqualTo(1);
    }

    @ParameterizedTest(name = "{0}에 {1}을 전달하면 {2}를 반환한다.")
    @CsvSource(value = {"2:2:true", "1:2:false", "2:1:true"}, delimiter = ':')
    void isGreaterThanOrEqualTo_메소드는_기존_거리에_비교할_거리를_전달하면_기존_거리가_더_크거나_같은지_여부를_반환한다(
            final int sourceDistanceValue,
            final int targetDistanceValue,
            final boolean expected
    ) {
        final Distance sourceDistance = Distance.from(sourceDistanceValue);
        final Distance targetDistance = Distance.from(targetDistanceValue);

        final boolean actual = sourceDistance.isGreaterThanOrEqualTo(targetDistance);

        assertThat(actual).isSameAs(expected);
    }

    @ParameterizedTest(name = "{0}에 {1}을 전달하면 {2}를 반환한다.")
    @CsvSource(value = {"2:2:false", "1:2:true", "2:1:false"}, delimiter = ':')
    void isGreaterThanOrEqualTo_메소드는_기존_거리에_비교할_거리를_전달하면_기존_거리가_작은지_여부를_반환한다(
            final int sourceDistanceValue,
            final int targetDistanceValue,
            final boolean expected
    ) {
        final Distance sourceDistance = Distance.from(sourceDistanceValue);
        final Distance targetDistance = Distance.from(targetDistanceValue);

        final boolean actual = sourceDistance.isLessThan(targetDistance);

        assertThat(actual).isSameAs(expected);
    }
}
