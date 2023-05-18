package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ColorTest {

    @ParameterizedTest
    @ValueSource(strings = {"", "#", "#1", "#123", "#12345G", "123456", "#GGGGGG"})
    @DisplayName("#일곱자리 16진수 색상표가 아니면 생성하지 못한다.")
    void validate_hex_color_code(String color) {
        // when + then
        assertThatThrownBy(() -> new Color(color))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"#000000", "#123456", "#AAAAAA", "#FFFFFF"})
    @DisplayName("#일곱자리 16진수 색상표여야 한다.")
    void generate_color_success(String color) {
        // when + then
        assertDoesNotThrow(() -> new Color(color));
    }
}