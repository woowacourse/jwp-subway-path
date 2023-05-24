package subway.domain.path.graph;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class PathGraphTest {

    @Test
    void findShortestPathSections_메소드는_출발_역과_도착_역을_전달하면_최단_거리를_반환한다() {
        final Station first = Station.of(1L, "1역");
        final Station second = Station.of(2L, "2역");
        final Station third = Station.of(3L, "3역");
        final Station fourth = Station.of(4L, "4역");
        final Station fifth = Station.of(5L, "5역");
        final Station sixth = Station.of(6L, "6역");
        final Station seventh = Station.of(7L, "7역");
        final Station eighth = Station.of(8L, "8역");
        final Station nineth = Station.of(9L, "9역");

        final Line firstLine = Line.of(1L, "1호선", "bg-red-500");

        firstLine.createSection(first, second, Distance.from(80), Direction.DOWN);
        firstLine.createSection(second, third, Distance.from(80), Direction.DOWN);
        firstLine.createSection(third, fourth, Distance.from(80), Direction.DOWN);
        firstLine.createSection(fourth, fifth, Distance.from(80), Direction.DOWN);
        firstLine.createSection(fifth, nineth, Distance.from(80), Direction.DOWN);

        final Line secondLine = Line.of(2L, "2호선", "bg-red-500");

        secondLine.createSection(second, sixth, Distance.from(1), Direction.DOWN);
        secondLine.createSection(sixth, seventh, Distance.from(1), Direction.DOWN);
        secondLine.createSection(seventh, eighth, Distance.from(1), Direction.DOWN);
        secondLine.createSection(eighth, fifth, Distance.from(1), Direction.DOWN);

        final PathGraph pathGraph = PathGraph.from(List.of(firstLine, secondLine));

        final List<PathEdges> actual = pathGraph.findShortestPathSections(first, nineth);

        assertAll(
                () -> assertThat(actual).hasSize(3),
                () -> assertThat(actual.get(0).getPathSections()).hasSize(1),
                () -> assertThat(actual.get(1).getPathSections()).hasSize(4),
                () -> assertThat(actual.get(2).getPathSections()).hasSize(1)
        );
    }
}
