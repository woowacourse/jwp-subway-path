package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 이름은_null_이거나_빈_값일_수_없다(final String name) {
        assertThatThrownBy(() -> new Line(name, "green"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선 이름이 입력되지 않았습니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 색깔은_null_이거나_빈_값일_수_없다(final String color) {
        assertThatThrownBy(() -> new Line("2호선", color))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("색깔이 입력되지 않았습니다.");
    }
}
