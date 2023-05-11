package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidColorException;

class ColorTest {

    @ParameterizedTest
    @DisplayName("색 이름이 존재하지 않으면 예외를 던진다.")
    @NullSource
    void validateWithNull(final String input) {
        assertThatThrownBy(() -> new Color(input))
                .isInstanceOf(InvalidColorException.class)
                .hasMessage("노선 색은 존재해야 합니다.");
    }

    @ParameterizedTest
    @DisplayName("색 이름 형식에 맞지 않는다면 예외를 던진다.")
    @ValueSource(strings = {"색", "하양", "white색", "하양하양하양하양하양하양색"})
    void validateWithInvalidColorFormat(final String input) {
        assertThatThrownBy(() -> new Color(input))
                .isInstanceOf(InvalidColorException.class)
                .hasMessage("색 이름은 '색'으로 끝나야 합니다.");
    }
}
