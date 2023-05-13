package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.InvalidLineNameException;

class NameTest {

    @Test
    @DisplayName("노선 이름을 정상적으로 생성한다.")
    void name() {
        assertDoesNotThrow(() -> new Name("5호선"));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("노선이 이름이 존재하지 않으면 예외를 던진다.")
    void validateWithBlank(final String input) {
        assertThatThrownBy(() -> new Name(input))
                .isInstanceOf(InvalidLineNameException.class)
                .hasMessage("노선 이름은 존재해야 합니다.");
    }

    @ParameterizedTest
    @DisplayName("노선의 이름 형식이 맞지 않을 경우 예외를 던진다.")
    @ValueSource(strings = {"3", "삼호선", "0호선", "A호선", "12호선"})
    void validateWithInvalidNameFormat(final String input) {
        assertThatThrownBy(() -> new Name(input))
                .isInstanceOf(InvalidLineNameException.class)
                .hasMessage("노선 이름은 1~9호선이어야 합니다.");
    }
}
