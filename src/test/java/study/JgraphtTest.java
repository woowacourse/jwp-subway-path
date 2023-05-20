package study;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.line.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("Jgrapht 학습 테스트")
public class JgraphtTest {

    @Test
    void 최단_경로를_구한다() {
        // given
        final WeightedMultigraph<String, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("a1");
        graph.addVertex("a2");
        graph.addVertex("a3");
        graph.addVertex("b1");
        graph.addVertex("b2");
        graph.setEdgeWeight(graph.addEdge("a1", "a2"), 2);
        graph.setEdgeWeight(graph.addEdge("a2", "a3"), 4);
        graph.setEdgeWeight(graph.addEdge("b1", "a2"), 6);
        graph.setEdgeWeight(graph.addEdge("a2", "b2"), 6);

        final DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath =
                new DijkstraShortestPath<>(graph);

        // when
        final List<String> vertexList = shortestPath.getPath("b1", "a1")
                .getVertexList();

        // then
        assertThat(vertexList)
                .containsExactly("b1", "a2", "a1");
    }

    @Test
    void 최단_경로를_구한다2() {
        // given
        final WeightedMultigraph<Station, SectionAdapter> graph
                = new WeightedMultigraph<>(SectionAdapter.class);
        final Station a1 = new Station("a1");
        final Station a2 = new Station("a2");
        final Station a3 = new Station("a3");
        final Station b1 = new Station("b1");
        final Station b2 = new Station("b2");

        graph.addVertex(a1);
        graph.addVertex(a2);
        graph.addVertex(a3);
        graph.addVertex(b1);
        graph.addVertex(b2);
        graph.setEdgeWeight(graph.addEdge(a1, a2), 2);
        graph.setEdgeWeight(graph.addEdge(a2, a3), 4);
        graph.setEdgeWeight(graph.addEdge(b1, a2), 6);
        graph.setEdgeWeight(graph.addEdge(a2, b2), 8);

        final DijkstraShortestPath<Station, SectionAdapter> shortestPath =
                new DijkstraShortestPath<>(graph);

        // when
        final List<Station> vertexList = shortestPath.getPath(b1, a1)
                .getVertexList();
        final List<SectionAdapter> edgeList = shortestPath.getPath(b1, a1).getEdgeList();

        // then
        final List<Section> sections = edgeList.stream()
                .map(SectionAdapter::toSection)
                .collect(Collectors.toList());
        assertThat(vertexList)
                .extracting(Station::name)
                .containsExactly("b1", "a2", "a1");
        assertThat(sections)
                .extracting(it -> it.up().name() + "-[" + it.distance() + "km]-" + it.down().name())
                .containsExactly(
                        "b1-[6km]-a2", "a1-[2km]-a2"
                );
    }

    @Test
    void 최단_경로를_구한다3() {
        // given
        final Station a1 = new Station("a1");
        final Station a2 = new Station("a2");
        final Station a3 = new Station("a3");
        final Station b1 = new Station("b1");
        final Station b2 = new Station("b2");
        final List<Line> lines = of(
                new Line("1", 0,
                        new Section(a1, a2, 2),
                        new Section(a2, a3, 4)
                ),
                new Line("2", 0,
                        new Section(b1, a2, 6),
                        new Section(a2, b2, 8)
                )
        );
        final RouteGraphAdapter graph = RouteGraphAdapter.adapt(lines);
        final DijkstraShortestPath<Station, SectionAdapter> shortestPath = new DijkstraShortestPath<>(graph);

        // when
        final List<Station> vertexList = shortestPath.getPath(b1, a1)
                .getVertexList();
        final List<SectionAdapter> edgeList = shortestPath.getPath(b1, a1).getEdgeList();

        // then
        final List<Section> sections = edgeList.stream()
                .map(SectionAdapter::toSection)
                .collect(Collectors.toList());
        assertThat(vertexList)
                .extracting(Station::name)
                .containsExactly("b1", "a2", "a1");
        assertThat(sections)
                .extracting(it -> it.up().name() + "-[" + it.distance() + "km]-" + it.down().name())
                .containsExactly(
                        "b1-[6km]-a2", "a1-[2km]-a2"
                );
    }

    @Test
    void 최단_경로가_여러개인_경우_먼저_등록된놈이_나온다() {
        final WeightedMultigraph<String, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("a1");
        graph.addVertex("a2");
        graph.addVertex("a3");
        graph.addVertex("a4");
        graph.setEdgeWeight(graph.addEdge("a1", "a3"), 2);
        graph.setEdgeWeight(graph.addEdge("a3", "a4"), 2);
        graph.setEdgeWeight(graph.addEdge("a1", "a2"), 2);
        graph.setEdgeWeight(graph.addEdge("a2", "a4"), 2);

        final DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath =
                new DijkstraShortestPath<>(graph);

        // when
        final List<String> vertexList = shortestPath.getPath("a1", "a4")
                .getVertexList();

        // then
        assertThat(vertexList)
                .containsExactly("a1", "a3", "a4");
    }

    @Test
    void 경로를_구할_수_없는_경우() {
        final WeightedMultigraph<String, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("a1");
        graph.addVertex("a2");
        graph.addVertex("b1");
        graph.addVertex("b2");
        graph.setEdgeWeight(graph.addEdge("a1", "a2"), 2);
        graph.setEdgeWeight(graph.addEdge("b1", "b2"), 2);

        final DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath =
                new DijkstraShortestPath<>(graph);

        // when
        assertThat(shortestPath.getPath("a1", "b1"))
                .isNull();
    }

    @Test
    void 출발지와_목적지가_동일하다면() {
        // given
        final WeightedMultigraph<String, DefaultWeightedEdge> graph =
                new WeightedMultigraph<>(DefaultWeightedEdge.class);
        graph.addVertex("a1");
        graph.addVertex("a2");
        graph.setEdgeWeight(graph.addEdge("a1", "a2"), 2);

        final DijkstraShortestPath<String, DefaultWeightedEdge> shortestPath =
                new DijkstraShortestPath<>(graph);

        // when
        final List<DefaultWeightedEdge> edgeList = shortestPath.getPath("a1", "a1").getEdgeList();
        final List<String> vertexList = shortestPath.getPath("a1", "a1").getVertexList();

        // then
        assertThat(edgeList.size()).isEqualTo(0);
        assertThat(vertexList.size()).isEqualTo(1);
    }
}
