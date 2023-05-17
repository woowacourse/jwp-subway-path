package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.LineNameValidateLengthException;

class LineNameTest {

    @DisplayName("이름 길이가 20자가 넘는 경우 예외처리")
    @Test
    void validateNameLength() {
        assertThatThrownBy(() -> new LineName(" ".repeat(21)))
                .isInstanceOf(LineNameValidateLengthException.class);
    }
}
