package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.DistanceNotValidException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DistanceTest {

    @ParameterizedTest(name = "양수가 아닌 값을 입력받는 경우 예외가 발생한다. 입력값: {0}")
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
}
