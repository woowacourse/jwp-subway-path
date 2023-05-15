package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineNameTest {

    @DisplayName("이름 길이가 20자가 넘는 경우 예외처리")
    @Test
    void validateNameLength() {
        assertThatThrownBy(() -> new LineName(" ".repeat(21)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 이름의 길이는 20자 이하여야 합니다.");
    }
}
