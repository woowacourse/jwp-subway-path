package subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.line.domain.line.LineName;
import subway.line.domain.line.exception.LineNameException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("노선 이름은")
class LineNameTest {

    @Test
    void 정상적으로_생성된다() {
        final String input = "강남역";

        assertThatCode(() -> new LineName(input))
                .doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "입력값: {0}")
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    void 공백이_아니어야_한다(String input) {
        assertThatCode(() -> new LineName(input))
                .isInstanceOf(LineNameException.class)
                .hasMessage("노선 이름이 공백입니다. 글자를 입력해주세요");
    }

    @Test
    void 최대_글자를_초과하면_안된다() {
        String input = "a".repeat(256);

        assertThatCode(() -> new LineName(input))
                .isInstanceOf(LineNameException.class)
                .hasMessage("노선 이름이 255글자를 초과했습니다");
    }
}
