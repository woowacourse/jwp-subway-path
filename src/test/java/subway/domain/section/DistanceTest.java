package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("만약 1 ~ 100 사이가 아닌 길이를 전달하면 예외가 발생한다.")
    void given_invalidRangeDistance_when_callFromMethod_then_throwException(final int invalidDistance) {

        assertThatThrownBy(() -> Distance.from(invalidDistance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("길이는 1 이상, 100 이하만 가능합니다.");
    }

    @Test
    @DisplayName("만약 기존 거리에 더할 거리를 전달하면 합을 반환한다.")
    void given_addDistance_when_callAddMethod_then_returnAddResult() {
        final Distance first = Distance.from(1);
        final Distance second = Distance.from(2);

        final Distance actual = first.add(second);

        assertThat(actual.getDistance()).isEqualTo(3);
    }

    @Test
    @DisplayName("만약 기존 거리에 뺄 거리를 전달하면 합을 반환한다.")
    void given_minusDistance_when_callMinusMethod_then_returnMinusResult() {
        final Distance first = Distance.from(2);
        final Distance second = Distance.from(1);

        final Distance actual = first.minus(second);

        assertThat(actual.getDistance()).isEqualTo(1);
    }

    @ParameterizedTest(name = "{0}을 전달하면 {1}을 반환한다.")
    @CsvSource(value = {"2:2:true", "1:2:false", "2:1:true"}, delimiter = ':')
    @DisplayName("만약 기존 거리에 비교할 거리를 전달하면 기존 거리가 더 크거나 같은지 여부를 반환한다.")
    void given_minusDistance_when_callMinusMethod_then_returnMinusResult(
            final int sourceDistanceValue,
            final int targetDistanceValue,
            final boolean expected
    ) {
        final Distance sourceDistance = Distance.from(sourceDistanceValue);
        final Distance targetDistance = Distance.from(targetDistanceValue);

        final boolean actual = sourceDistance.isGreaterThanOrEqualTo(targetDistance);

        assertThat(actual).isSameAs(expected);
    }
}
