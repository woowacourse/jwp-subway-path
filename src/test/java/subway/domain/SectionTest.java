package subway.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionTest {
    @Test
    @DisplayName("생성한다.")
    void create() {
        // given
        Line line = new Line("2호선");
        Station sourceStation = new Station("선릉역", List.of(line));
        Station targetStation = new Station("잠실역", List.of(line));
        int distance = 10;

        // then
        assertDoesNotThrow(() -> new Section(sourceStation, targetStation, distance, line));
    }
}
