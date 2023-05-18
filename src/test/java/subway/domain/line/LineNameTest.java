package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@SuppressWarnings("NonAsciiCharacters")
class LineNameTest {

    @ParameterizedTest(name = "노선 이름은 공백일 수 없다.")
    @ValueSource(strings = {" ", "    ", ""})
    void 노선_이름은_공백일_수_없다(final String input) {
        assertThatThrownBy(() -> new LineName(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
