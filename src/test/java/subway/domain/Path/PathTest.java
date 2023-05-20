package subway.domain.Path;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.domain.Path.PathTestFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

@DisplayName("Path 기능 테스트")
class PathTest {

    @Test
    @DisplayName("경로는 여러개의 역들로 구성된다.")
    void createPathTest() {
        // given
        List<Station> orderedStation = new ArrayList<>(List.of(강변, 잠실나루, 잠실, 몽촌토성, 강동구청));

        // then
        assertDoesNotThrow(() -> Path.from(orderedStation));
    }
}
