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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.김풍;
import static subway.common.fixture.DomainFixture.김풍_후추;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.디노_로운;
import static subway.common.fixture.DomainFixture.디노_조앤;
import static subway.common.fixture.DomainFixture.로운;
import static subway.common.fixture.DomainFixture.로운_김풍;
import static subway.common.fixture.DomainFixture.조앤;
import static subway.common.fixture.DomainFixture.조앤_주호민;
import static subway.common.fixture.DomainFixture.주호민;
import static subway.common.fixture.DomainFixture.침착맨;
import static subway.common.fixture.DomainFixture.침착맨_디노;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.DomainFixture.후추_디노;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class JGraphTTest {

    public static final Line 일호선_남색_후추_디노_조앤_주호민 = new Line(1L, "일호선", "남색",
            List.of(new Section(후추, 디노, 7),
                    new Section(디노, 조앤, 4),
                    new Section(조앤, 주호민, 5)
            ));
    public static final Line 이호선_초록색_침착맨_디노_로운_김풍_후추 = new Line(2L, "이호선", "초록색",
            List.of(new Section(침착맨, 디노, 5),
                    new Section(디노, 로운, 2),
                    new Section(로운, 김풍, 4),
                    new Section(김풍, 후추, 1)
            ));

    @Test
    void 사용해보기() {
        final List<Line> lines = new ArrayList<>(List.of(일호선_남색_후추_디노_조앤_주호민, 이호선_초록색_침착맨_디노_로운_김풍_후추));

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

    @Test
    void 상속해보기() {
        //given
        final WeightedMultigraph<Station, SectionExample> graph = new WeightedMultigraph<>(SectionExample.class);

        graph.addVertex(후추);
        graph.addVertex(디노);
        graph.addVertex(조앤);
        graph.addVertex(로운);
        graph.addVertex(침착맨);
        graph.addVertex(주호민);
        graph.addVertex(김풍);

        final SectionExample edge1 = new SectionExample(일호선_남색_후추_디노_조앤_주호민.getId(), 후추_디노);
        final SectionExample edge2 = new SectionExample(일호선_남색_후추_디노_조앤_주호민.getId(), 디노_조앤);
        final SectionExample edge3 = new SectionExample(일호선_남색_후추_디노_조앤_주호민.getId(), 조앤_주호민);
        final SectionExample edge4 = new SectionExample(이호선_초록색_침착맨_디노_로운_김풍_후추.getId(), 침착맨_디노);
        final SectionExample edge5 = new SectionExample(이호선_초록색_침착맨_디노_로운_김풍_후추.getId(), 디노_로운);
        final SectionExample edge6 = new SectionExample(이호선_초록색_침착맨_디노_로운_김풍_후추.getId(), 로운_김풍);
        final SectionExample edge7 = new SectionExample(이호선_초록색_침착맨_디노_로운_김풍_후추.getId(), 김풍_후추);

        graph.addEdge(edge1.getFrom(), edge1.getTo(), edge1);
        graph.addEdge(edge2.getFrom(), edge2.getTo(), edge2);
        graph.addEdge(edge3.getFrom(), edge3.getTo(), edge3);
        graph.addEdge(edge4.getFrom(), edge4.getTo(), edge4);
        graph.addEdge(edge5.getFrom(), edge5.getTo(), edge5);
        graph.addEdge(edge6.getFrom(), edge6.getTo(), edge6);
        graph.addEdge(edge7.getFrom(), edge7.getTo(), edge7);

        graph.setEdgeWeight(edge1, edge1.getDistance());
        graph.setEdgeWeight(edge2, edge2.getDistance());
        graph.setEdgeWeight(edge3, edge3.getDistance());
        graph.setEdgeWeight(edge4, edge4.getDistance());
        graph.setEdgeWeight(edge5, edge5.getDistance());
        graph.setEdgeWeight(edge6, edge6.getDistance());
        graph.setEdgeWeight(edge7, edge7.getDistance());

        //when
        final DijkstraShortestPath<Station, SectionExample> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
        final GraphPath<Station, SectionExample> path = dijkstraShortestPath.getPath(침착맨, 주호민);

        //then
        final List<Section> sections = path.getEdgeList().stream().map(SectionExample::toSection).collect(Collectors.toList());
        assertSoftly(softly -> {
            softly.assertThat(sections).usingRecursiveComparison()
                    .isEqualTo(List.of(
                            침착맨_디노,
                            디노_조앤,
                            조앤_주호민
                    ));
            softly.assertThat(path.getWeight()).isEqualTo(14);
        });
    }
}
