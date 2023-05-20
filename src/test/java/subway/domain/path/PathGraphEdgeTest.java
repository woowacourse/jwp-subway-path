package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
class PathGraphEdgeTest {

    Station sourceStation;
    Station targetStation;
    Line line;
    PathEdge pathEdge;

    @BeforeEach
    void setUp() {
        sourceStation = Station.of(1L, "1역");
        targetStation = Station.of(2L, "2역");
        line = Line.of("1호선", "bg-red-500");
        line.createSection(sourceStation, targetStation, Distance.from(5), Direction.DOWN);
        pathEdge = PathEdge.of(sourceStation, targetStation, line);
    }

    @Test
    void of_메소드는_동일한_역을_전달하면_예외가_발생한다() {
        assertThatThrownBy(() -> PathEdge.of(sourceStation, sourceStation, line))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 역으로 이동할 수 없습니다.");
    }

    @Test
    void of_메소드는_전달하는_line에_등록되지_않은_역을_전달하면_예외가_발생한다() {
        final Station station = Station.of(3L, "3역");

        assertThatThrownBy(() -> PathEdge.of(sourceStation, station, line))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 노선에 등록되지 않은 역입니다.");
    }

    @Test
    void getWeight_메소드는_호출하면_line에_등록된_station의_구역을_찾아_거리를_반환한다() {
        final double actual = pathEdge.getWeight();

        assertThat(actual).isEqualTo(5.0d);
    }
}
