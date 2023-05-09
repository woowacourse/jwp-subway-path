package subway.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationTest {
    @Test
    @DisplayName("생성한다.")
    void create() {
        // given
        List<Line> lines = List.of(new Line("2호선"));

        // then
        assertDoesNotThrow(() -> new Station("봉천역", lines));
    }
}
