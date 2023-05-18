package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import subway.domain.line.Color;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ColorTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 색깔은_null_이거나_빈_값일_수_없다(final String color) {
        assertThatThrownBy(() -> new Color(color))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("색깔이 입력되지 않았습니다.");
    }
}
