package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Subway;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SubwayGraphTest {

    Station A_STATION = new Station(1L, "A");
    Station B_STATION = new Station(2L, "B");
    Station C_STATION = new Station(3L, "C");

    @Test
    void 구간으로_그래프를_등록할_수_있다() {
        Line firstLine = new Line("1호선", List.of(
                new Section(A_STATION, B_STATION, 5),
                new Section(B_STATION, C_STATION, 10)
        ));
        Line secondLine = new Line("2호선", List.of(
                new Section(A_STATION, C_STATION, 9)
        ));

        SubwayGraph subwayGraph = SubwayGraph.from(new Subway(List.of(firstLine, secondLine)));

        assertThat(subwayGraph.vertexSet())
                .usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(Set.of(
                        B_STATION,
                        A_STATION,
                        C_STATION
                ));
    }

    @Test
    void 최단_경로와_거리를_조회한다() {
        Line firstLine = new Line("1호선", List.of(
                new Section(A_STATION, B_STATION, 5),
                new Section(B_STATION, C_STATION, 10)
        ));
        Line secondLine = new Line("2호선", List.of(
                new Section(A_STATION, C_STATION, 9)
        ));

        SubwayGraph subwayGraph = SubwayGraph.from(new Subway(List.of(firstLine, secondLine)));

        ShortestPath path = subwayGraph.findPath(A_STATION, C_STATION);
        assertThat(path).usingRecursiveComparison()
                .isEqualTo(new ShortestPath(
                        List.of(A_STATION, C_STATION),
                        9
                ));
    }

    @Test
    void 같은_거리의_경로면_지나가는_역이_제일_작은_경로가_조회된다() {
        Line firstLine = new Line("1호선", List.of(
                new Section(A_STATION, B_STATION, 4),
                new Section(B_STATION, C_STATION, 5)
        ));
        Line secondLine = new Line("2호선", List.of(
                new Section(A_STATION, C_STATION, 9)
        ));

        SubwayGraph subwayGraph = SubwayGraph.from(new Subway(List.of(firstLine, secondLine)));

        ShortestPath path = subwayGraph.findPath(A_STATION, C_STATION);

        assertThat(path).usingRecursiveComparison()
                .isEqualTo(new ShortestPath(
                        List.of(
                                A_STATION, C_STATION
                        ),
                        9
                ));
    }
}
