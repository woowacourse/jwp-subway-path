package subway.domain.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.List;

import static fixtures.LineFixtures.LINE2;
import static fixtures.LineFixtures.LINE7;
import static org.assertj.core.api.Assertions.assertThat;

class PathFinderTest {

    @Test
    @DisplayName("최단 경로를 찾는다.")
    void findShortestPathTest() {
        // given
        Sections sections = new Sections(List.of(
                new Section(null, new Station(null, "a", LINE2), new Station(null, "b", LINE2), 10),
                new Section(null, new Station(null, "b", LINE2), new Station(null, "c", LINE2), 15),
                new Section(null, new Station(null, "b", LINE7), new Station(null, "d", LINE7), 1),
                new Section(null, new Station(null, "d", LINE7), new Station(null, "c", LINE7), 2))
        );
        Path expectPath = new Path(List.of("a", "b", "d", "c"), 13);

        // when
        Path path = PathFinder.findPath(sections, "a", "c");

        // then
        assertThat(path).isEqualTo(expectPath);
    }
}