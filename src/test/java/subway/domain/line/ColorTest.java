package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidColorException;

class ColorTest {

    @ParameterizedTest
    @DisplayName("색 이름이 존재하지 않으면 예외를 던진다.")
    @NullAndEmptySource
    void validateWithNull(final String input) {
        assertThatThrownBy(() -> new Color(input))
                .isInstanceOf(InvalidColorException.class)
                .hasMessage("노선 색은 존재해야 합니다.");
    }

    @Test
    @DisplayName("색 이름이 최대 길이를 넘으면 예외를 던진다.")
    void validateWithLength() {
        final String input = "열한글자가넘는색이름입니다색";

        assertThatThrownBy(() -> new Color(input))
                .isInstanceOf(InvalidColorException.class)
                .hasMessage("노선 색은 11글자까지 가능합니다.");
    }

    @ParameterizedTest
    @DisplayName("색 이름 형식에 맞지 않는다면 예외를 던진다.")
    @ValueSource(strings = {"색", "하양", "white색", "123색"})
    void validateWithInvalidColorFormat(final String input) {
        assertThatThrownBy(() -> new Color(input))
                .isInstanceOf(InvalidColorException.class)
                .hasMessage("색 이름은 한글로 이루어져 있고, '색'으로 끝나야 합니다.");
    }
}
