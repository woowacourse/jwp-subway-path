package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @ValueSource(strings = {"가1", "가나다라마바사아자"})
    @ParameterizedTest
    void 정상적인_값이_이름으로_들어오면_역을_생성한다(final String input) {
        assertDoesNotThrow(() -> Station.from(input));
    }

    @ValueSource(strings = {"test", "1q2q", "가나다a"})
    @ParameterizedTest
    void 한글과_숫자가_아닌_값이_이름으로_들어오면_예외가_발생한다(final String input) {

        assertThatThrownBy(() -> Station.from(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 이름은 한글과 숫자만 가능합니다.");
    }

    @ValueSource(strings = {"가", "가나다라마바사123"})
    @ParameterizedTest
    void 길이가_2_이상_9_이하가_아닌_값이_이름으로_들어오면_예외가_발생한다(final String input) {

        assertThatThrownBy(() -> Station.from(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 이름은 2글자 ~ 9글자만 가능합니다.");
    }
}
