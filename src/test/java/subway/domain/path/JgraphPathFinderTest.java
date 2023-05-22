package subway.domain.path;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.domain.station.StationName;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
class JgraphPathFinderTest {

    @Test
    void 출발역과_도착역을_받아_경로를_찾는다() {
        //given
        final Station A = new Station(1L, new StationName("A"));
        final Station B = new Station(2L, new StationName("B"));
        final Station C = new Station(3L, new StationName("C"));
        final Section section1 = new Section(1L, A, B, new Distance(5));
        final Section section2 = new Section(2L, B, C, new Distance(6));

        final Line line = new Line(1L, new LineName("1호선"), new LineColor("파랑"),
                new Sections(List.of(section1, section2)));

        final JgraphPathFinder jgraphPathFinder = new JgraphPathFinder(List.of(line));

        final Path path = jgraphPathFinder.findPath(A, C);
        path.getPathStations().stations().stream().forEach(station -> System.out.println(station.getName().name()));
    }
}
