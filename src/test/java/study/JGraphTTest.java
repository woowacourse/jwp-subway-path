package study;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.이호선_초록색_침착맨_디노_로운_우원박_후추;
import static subway.common.fixture.DomainFixture.일호선_남색_후추_디노_조앤_주호민;
import static subway.common.fixture.DomainFixture.주호민;
import static subway.common.fixture.DomainFixture.후추;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class JGraphTTest {

    @Test
    void 사용해보기() {

        final List<Line> lines = new ArrayList<>(List.of(일호선_남색_후추_디노_조앤_주호민, 이호선_초록색_침착맨_디노_로운_우원박_후추));

        final WeightedMultigraph<Station, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        final Line line1 = lines.get(0);
        final Line line2 = lines.get(1);

        Stream.concat(line1.getOrderedStations().stream(), line2.getOrderedStations().stream())
                .distinct()
                .forEach(graph::addVertex);

        for (Line line : lines) {
            final List<Section> sections = line.getSections();
            sections.forEach(section -> graph.setEdgeWeight(graph.addEdge(section.getFrom(), section.getTo()), section.getDistanceValue()));
        }

        final DijkstraShortestPath<Station, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final List<Station> vertexList = dijkstraShortestPath.getPath(주호민, 후추).getVertexList();
        System.out.println("vertexList = " + vertexList);
        final List<DefaultWeightedEdge> edgeList = dijkstraShortestPath.getPath(주호민, 후추).getEdgeList();
        System.out.println("edgeList = " + edgeList);
        final double weight = dijkstraShortestPath.getPath(주호민, 후추).getWeight();
        System.out.println("weight = " + weight);
        final Graph<Station, DefaultWeightedEdge> graph1 = dijkstraShortestPath.getPath(주호민, 후추).getGraph();
        System.out.println("graph1 = " + graph1);
    }

    @Test
    void 가중치가_같은_경우_더_적은_정점을_지난다() {
        //given
        final WeightedMultigraph<String, DefaultWeightedEdge> graph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

        graph.addVertex("침착맨");
        graph.addVertex("주호민");
        graph.addVertex("김풍");
        graph.addVertex("박정민");
        graph.addVertex("주우재");
        graph.addVertex("통닭천사");
        graph.setEdgeWeight(graph.addEdge("침착맨", "주호민"), 3);
        graph.setEdgeWeight(graph.addEdge("주호민", "김풍"), 3);
        graph.setEdgeWeight(graph.addEdge("김풍", "통닭천사"), 3);
        graph.setEdgeWeight(graph.addEdge("통닭천사", "주우재"), 3);
        graph.setEdgeWeight(graph.addEdge("주호민", "박정민"), 3);
        graph.setEdgeWeight(graph.addEdge("박정민", "주우재"), 6);

        //when
        final DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<String, DefaultWeightedEdge> path = dijkstraShortestPath.getPath("침착맨", "주우재");

        //then
        assertSoftly(softly -> {
            softly.assertThat(path.getVertexList()).containsExactly("침착맨", "주호민", "박정민", "주우재");
            softly.assertThat(path.getWeight()).isEqualTo(12);
        });
    }
}
