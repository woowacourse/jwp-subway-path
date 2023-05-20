package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.section.Direction;
import subway.domain.section.Distance;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings({"NonAsciiCharacters", "SpellCheckingInspection"})
class PathGraphEdgesTest {

    PathEdges pathEdges;

    @BeforeEach
    void setUp() {
        final Station sourceStation = Station.of(1L, "1역");
        final Station targetStation = Station.of(2L, "2역");
        final Line line = Line.of(1L, "1호선", "bg-red-500");
        line.createSection(sourceStation, targetStation, Distance.from(5), Direction.DOWN);
        final PathEdge pathEdge = PathEdge.of(sourceStation, targetStation, line);
        pathEdges = PathEdges.create();
        pathEdges.add(pathEdge);
    }

    @Test
    void isOtherLine_메소드는_pathSections에_저장한_pathSection과_다른_line의_pathSection을_전달하면_true를_반환한다() {
        final Station sourceStation = Station.of(3L, "3역");
        final Station targetStation = Station.of(4L, "4역");
        final Line line = Line.of(2L, "2호선", "bg-red-500");
        line.createSection(sourceStation, targetStation, Distance.from(5), Direction.DOWN);
        final PathEdge pathEdge = PathEdge.of(sourceStation, targetStation, line);

        final boolean actual = pathEdges.isOtherLine(pathEdge);

        assertThat(actual).isTrue();
    }

    @Test
    void isOtherLine_메소드는_pathSections에_저장한_pathSection과_같은_line의_pathSection을_전달하면_false를_반환한다() {
        final Station sourceStation = Station.of(1L, "1역");
        final Station targetStation = Station.of(2L, "2역");
        final Line line = Line.of(1L, "1호선", "bg-red-500");
        line.createSection(sourceStation, targetStation, Distance.from(5), Direction.DOWN);
        final PathEdge pathEdge = PathEdge.of(sourceStation, targetStation, line);

        final boolean actual = pathEdges.isOtherLine(pathEdge);

        assertThat(actual).isFalse();
    }

    @Test
    void calculateTotalPathSectionDistance_메소드는_호출하면_모든_pathSection의_길이를_더해서_반환한다() {
        final int actual = pathEdges.calculateTotalPathSectionDistance();

        assertThat(actual).isEqualTo(5);
    }
}
