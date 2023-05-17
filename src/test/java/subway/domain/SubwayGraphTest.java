package subway.domain;

import org.assertj.core.api.Assertions;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import subway.Fixture;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class SubwayGraphTest {

    @Test
    void findPath() {
        SubwayGraph subwayGraph = new SubwayGraph();

        List<Section> sections = List.of(
                Fixture.sectionAB,
                Fixture.sectionBC,
                Fixture.sectionCD,
                Fixture.sectionDE,
                Fixture.sectionAF,
                Fixture.sectionFE
        );

        Line line = new Line(1L, "잠실역", "green", new Sections(sections));
        Lines lines = new Lines(List.of(line));

        List<Station> shortestPath = subwayGraph.findPath(Fixture.stationA, Fixture.stationE, lines);

        Assertions.assertThat(shortestPath).containsExactly(Fixture.stationA, Fixture.stationF, Fixture.stationE);
    }
}