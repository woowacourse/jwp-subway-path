package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
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

    @Test
    void 구간으로_그래프를_등록할_수_있다() {
        Line firstLine = new Line("1호선", List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 10)
        ));
        Line secondLine = new Line("2호선", List.of(
                new Section("A", "C", 9)
        ));

        SubwayGraph subwayGraph = SubwayGraph.from(new Subway(List.of(firstLine, secondLine)));

        assertThat(subwayGraph.vertexSet())
                .containsOnly(
                        new Station("A"),
                        new Station("B"),
                        new Station("C")
                );
    }

    @Test
    void 최단_경로와_거리를_조회한다() {
        Line firstLine = new Line("1호선", List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 10)
        ));
        Line secondLine = new Line("2호선", List.of(
                new Section("A", "C", 9)
        ));

        SubwayGraph subwayGraph = SubwayGraph.from(new Subway(List.of(firstLine, secondLine)));

        ShortestPath path = subwayGraph.findPath(new Station("A"), new Station("C"));
        assertThat(path).usingRecursiveComparison()
                .isEqualTo(new ShortestPath(
                        List.of(new Station("A"), new Station("C")),
                        9
                ));
    }

    @Test
    void 같은_거리의_경로면_지나가는_역이_제일_작은_경로가_조회된다() {
        Line firstLine = new Line("1호선", List.of(
                new Section("A", "B", 4),
                new Section("B", "C", 5)
        ));
        Line secondLine = new Line("2호선", List.of(
                new Section("A", "C", 9)
        ));

        SubwayGraph subwayGraph = SubwayGraph.from(new Subway(List.of(firstLine, secondLine)));

        ShortestPath path = subwayGraph.findPath(new Station("A"), new Station("C"));

        assertThat(path).usingRecursiveComparison()
                .isEqualTo(new ShortestPath(
                        List.of(new Station("A"), new Station("C")),
                        9
                ));
    }
}
