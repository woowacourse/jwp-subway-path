package subway.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {
    @Test
    @DisplayName("생성한다.")
    void create() {
        assertDoesNotThrow(() -> new Line("1호선"));
    }
}
