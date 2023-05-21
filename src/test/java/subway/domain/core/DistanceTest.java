package subway.domain.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.DistanceNotValidException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DistanceTest {

    @ParameterizedTest(name = "양수가 아닌 값을 입력받는 경우 예외가 발생한다. 입력: {0}")
    @ValueSource(ints = {0, -1})
    void 양수가_아닌_값을_입력받는_경우_예외가_발생한다(final int value) {
        // expect
        assertThatThrownBy(() -> new Distance(value))
                .isInstanceOf(DistanceNotValidException.class)
                .hasMessage("거리 값은 양수여야 합니다.");
    }

    @Test
    void 양수를_입력받는_경우_정상적으로_생성된다() {
        // given
        int value = 1;

        // when
        final Distance distance = new Distance(value);

        // then
        assertThat(distance.getValue()).isEqualTo(1);
    }

    @ParameterizedTest(name = "3보다 크거나 같은지 확인한다. 입력: {0}, 결과: {1}")
    @CsvSource({"2, false", "3, true", "4, true"})
    void 자신의_거리보다_크거나_같은지_확인한다(final int value, final boolean result) {
        // given
        final Distance distance = new Distance(3);

        // expect
        assertThat(distance.moreThanOrEqual(new Distance(value))).isEqualTo(result);
    }

    @Test
    void 입력받은_거리를_뺀_값을_반환한다() {
        // given
        final Distance distance = new Distance(5);

        // when
        final Distance result = distance.subtract(new Distance(2));

        // then
        assertThat(result).isEqualTo(new Distance(3));
    }

    @Test
    void 입력받은_거리를_더한_값을_반환한다() {
        // given
        final Distance distance = new Distance(5);

        // when
        final Distance result = distance.add(new Distance(2));

        // then
        assertThat(result).isEqualTo(new Distance(7));
    }
}
