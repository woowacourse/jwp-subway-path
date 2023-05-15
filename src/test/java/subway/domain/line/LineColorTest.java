package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class LineColorTest {

    @ParameterizedTest(name = "색상으로 {0}를 전달하면 예외가 발생한다.")
    @ValueSource(strings = {"bt-red-500", "bg-500-red", "bg-red-50000"})
    void from_메소드는_색상으로_tailwindcss형식이_아닌_문자열을_전달하면_예외가_발생한다(final String invalidColor) {
        assertThatThrownBy(() -> LineColor.from(invalidColor))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 색상은 tailwindcss 형식만 가능합니다.");
    }
}
