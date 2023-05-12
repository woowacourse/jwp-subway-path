package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineTest {

    @DisplayName("역을 가질 수 있다")
    @Test
    void createLine() {
        //given
        final Station station = new Station("서면역");

        //when, then
        assertDoesNotThrow(() -> new Line("1호선", "red", List.of(station)));
    }
}
