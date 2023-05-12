package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @DisplayName("역을 순서대로 정렬한다.")
    @Test
    void sortTest() {
        final Line line = new Line(1L, "1호선", "파랑", Map.of(
                new Station(1L, "성남"),
                new Path(new Station(3L, "성대"), 5)
                , new Station(3L, "성대"),
                new Path(new Station(2L, "강남"), 10)));

        final List<Station> stations = line.sortStations();

        assertThat(stations).map(Station::getName).containsExactly("성남", "성대", "강남");
    }

}
