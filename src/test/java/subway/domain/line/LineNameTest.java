package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.IllegalLineNameException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineNameTest {

    @Test
    @DisplayName("노선의 이름이 10자 초과면 예외가 발생한다.")
    void validateLineNameLengthTest() {
        // given
        String invalidLineName = "가나다라마바사아자차카타파하호선";

        // when, then
        assertThatThrownBy(() -> new LineName(invalidLineName))
                .isInstanceOf(IllegalLineNameException.class)
                .hasMessage("호선 이름은 10자 이하여야 합니다.");
    }
}