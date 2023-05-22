package subway.domain.subway;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

class SubwayTest {

    @Test
    void getLineMap() {
        Line line1 = new Line(1L, "1호선", "파랑", 0);
        Line line2 = new Line(2L, "2호선", "초록", 0);

        List<Line> lines = List.of(
            line1,
            line2
        );

        List<Station> stations = List.of(
            new Station(1L, "잠실역"),
            new Station(2L, "잠실나루역1"),
            new Station(3L, "잠실나루역2"),
            new Station(4L, "잠실나루역3"),
            new Station(5L, "잠실나루역4"),
            new Station(6L, "잠실나루역5"),
            new Station(7L, "잠실나루역6")
        );

        List<Section> sections = List.of(
            new Section(1L, 1L, 2L, 1L, 5),
            new Section(1L, 2L, 3L, 1L, 5),
            new Section(1L, 3L, 4L, 1L, 5),
            new Section(1L, 1L, 3L, 2L, 2),
            new Section(1L, 4L, 5L, 1L, 5),
            new Section(1L, 5L, 6L, 1L, 5),
            new Section(1L, 6L, 7L, 1L, 50)
        );

        Subway subway = new Subway(lines, stations, sections);
        Map<Line, List<Station>> lineMap = subway.getLineMap();
        assertAll(
            () -> assertThat(lineMap.get(line1).stream().map(Station::getId)).containsExactly(1L, 2L, 3L, 4L,
                5L, 6L, 7L),
            () -> assertThat(lineMap.get(line2).stream().map(Station::getId)).containsExactly(1L, 3L)
        );
    }
}
