package subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.line.domain.exception.LineColorException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("노선 색상은")
class LineColorTest {

    @Test
    void 정상적으로_생성된다() {
        assertThatCode(() -> new LineColor("red"))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    void 공백이면_예외가_발생한다(String input) {
        assertThatCode(() -> new LineColor(input)).isInstanceOf(LineColorException.class)
                .hasMessage("노선 색상이 공백입니다. 글자를 입력해주세요");
    }

    @Test
    void 글자수가_20글자를_초과하면_예외가_발생한다() {
        String input = "1".repeat(21);

        assertThatCode(() -> new LineColor(input))
                .isInstanceOf(LineColorException.class)
                .hasMessage("노선 색상이 20글자를 초과했습니다");
    }
}
