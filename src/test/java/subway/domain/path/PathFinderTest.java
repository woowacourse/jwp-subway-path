package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PathFinderTest {

    @Test
    @DisplayName("최단 경로와 거리를 반환한다.")
    void find_short_path_and_distance() {
        // given
        Station departure = new Station(1L, "잠실");
        Station arrival = new Station(3L, "강남");
        Sections sections = new Sections(List.of(
                new Section(new Station(1L, "잠실"), new Station(2L, "선릉"), 10),
                new Section(new Station(2L, "선릉"), new Station(3L, "강남"), 10),
                new Section(new Station(4L, "잠실"), new Station(5L, "을지로"), 3),
                new Section(new Station(5L, "을지로"), new Station(6L, "충무로"), 2),
                new Section(new Station(6L, "충무로"), new Station(3L, "강남"), 1)
        ));

        PathFinder pathFinder = new PathFinder();

        Path expect = new Path(List.of(
                new Station(1L, "잠실"),
                new Station(5L, "을지로"),
                new Station(6L, "충무로"),
                new Station(3L, "강남")),
                new Distance(6));

        // when
        Path result = pathFinder.findShortestPath(sections, departure, arrival);
        // then

        assertThat(result.getDistance()).isEqualTo(expect.getDistance());
        assertThat(result.getStations()).isEqualTo(expect.getStations());
    }
}
