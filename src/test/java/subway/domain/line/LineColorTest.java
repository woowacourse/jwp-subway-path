package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class LineColorTest {

    @Test
    void 노선_색은_공백일_시_예외가_발생한다() {
        // given
        final String color = " ";

        assertThatThrownBy(() -> new LineColor(color))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 색상은 필수입니다.");
    }

    @Test
    void 노선_색은_공백이_아닐_시_정상생성된다() {
        // given
        final String color = "초록";

        assertDoesNotThrow(() -> new LineColor(color));
    }
}
