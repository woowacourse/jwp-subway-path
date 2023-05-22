package subway.domain.graph;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.Fixture;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;

import java.util.List;

class SubwayGraphTest {

    @Test
    void findPath() {

        final List<Section> sections = List.of(
                Fixture.sectionAB,
                Fixture.sectionBC,
                Fixture.sectionCD,
                Fixture.sectionDE,
                Fixture.sectionAF,
                Fixture.sectionFE
        );

        final Line line = new Line(1L, "잠실역", "green", new Sections(sections));
        final Lines lines = new Lines(List.of(line));
        final SubwayGraph subwayGraph = SubwayGraph.from(lines);

        final List<Station> shortestPath = subwayGraph.findPath(Fixture.stationA, Fixture.stationE);

        Assertions.assertThat(shortestPath).containsExactly(Fixture.stationA, Fixture.stationF, Fixture.stationE);
    }
}