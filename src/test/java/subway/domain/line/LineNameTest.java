package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class LineNameTest {

    @ParameterizedTest(name = "이름으로 {0}을 전달하면 예외가 발생한다.")
    @ValueSource(strings = {"a", "abcdefghij"})
    void from_메소드는_유효하지_않은_글자_수의_이름을_전달하면_예외가_발생한다(final String invalidName) {
        assertThatThrownBy(() -> LineName.from(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름은 2글자 ~ 9글자만 가능합니다.");
    }

    @ParameterizedTest(name = "이름으로 {0}을 전달하면 예외가 발생한다.")
    @ValueSource(strings = {"abc", "abc123de", "안녕hello"})
    void from_메소드는_유효하지_않은_형식의_이름을_전달하면_예외가_발생한다(final String invalidName) {
        assertThatThrownBy(() -> LineName.from(invalidName))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름은 한글과 숫자만 가능합니다.");
    }
}
