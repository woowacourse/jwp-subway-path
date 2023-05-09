package subway.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubwayTest {
    @Test
    @DisplayName("생성한다.")
    void create() {
        // given
        Line line = new Line("2호선");
        Station station1 = new Station("선릉역", List.of(line));
        Station station2 = new Station("잠실역", List.of(line));
        int distance = 10;

        List<Station> stations = new ArrayList<>();
        List<Section> sections = List.of(new Section(station1, station2, distance, line));
        stations.add(station1);
        stations.add(station2);

        // then
        assertDoesNotThrow(() -> new Subway(stations, sections));
    }

}
