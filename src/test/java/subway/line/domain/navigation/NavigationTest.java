package subway.line.domain.navigation;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.line.Line;
import subway.line.domain.navigation.domain.JgraphtGraph;
import subway.line.domain.section.Section;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class NavigationTest {
    private Station 반포역 = new Station(1L, "반포역");
    private Station 논현역 = new Station(2L, "논현역");
    private Station 학동역 = new Station(3L, "학동역");
    private Station 강남구청 = new Station(4L, "강남구청");
    private Station 청담역 = new Station(5L, "청담역");
    private Station 압구정로데오 = new Station(6L, "압구정로데오");
    private Station 서울숲 = new Station(7L, "서울숲");

    private final List<List<Section>> sectionsOfAllLine = List.of(
            List.of(
                    new Section(1L, 반포역, 논현역, Distance.of(3)),
                    new Section(2L, 논현역, 학동역, Distance.of(4)),
                    new Section(3L, 학동역, 강남구청, Distance.of(3)),
                    new Section(4L, 강남구청, 청담역, Distance.of(4)),
                    new Section(5L, 청담역, new EmptyStation(), new EmptyDistance())
            ),
            List.of(
                    new Section(6L, 강남구청, 압구정로데오, Distance.of(4)),
                    new Section(7L, 압구정로데오, 서울숲, Distance.of(5)),
                    new Section(8L, 서울숲, new EmptyStation(), new EmptyDistance())
            ));

    @Test
    @DisplayName("구간 정보를 이용하여 네비게이션을 만들 수 있다.")
    void makeNavigation() {
        final WeightedMultigraph<Station, DefaultWeightedEdge> weightedMultigraph = mock(WeightedMultigraph.class);

        final var graph = new JgraphtGraph(weightedMultigraph);
        assertThatCode(() -> new Navigation(sectionsOfAllLine, graph))
                .doesNotThrowAnyException();

        verify(weightedMultigraph, atLeastOnce()).addVertex(any(Station.class));
        verify(weightedMultigraph, atLeastOnce()).addEdge(any(Station.class), any(Station.class));
        verify(weightedMultigraph, atLeastOnce()).setEdgeWeight(nullable(DefaultWeightedEdge.class), any(Double.class));
    }

    @Test
    @DisplayName("같은 노선 위에 있는 두 역의 최단거리를 구할 수 있다.")
    void shortestDistance() {
        WeightedMultigraph<Station, DefaultWeightedEdge> jgraphtGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        final var graph = new JgraphtGraph(jgraphtGraph);
        final var navigation = new Navigation(sectionsOfAllLine, graph);

        assertThat(navigation.findShortestDistance(반포역, 청담역))
                .isEqualTo(Distance.of(14));
    }

    @Test
    @DisplayName("서로 다른 노선 위에 있는 두 역의 최단거리를 구할 수 있다.")
    void shortestDistanceOnDifferentLine() {
        WeightedMultigraph<Station, DefaultWeightedEdge> jgraphtGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        final var graph = new JgraphtGraph(jgraphtGraph);
        final var navigation = new Navigation(sectionsOfAllLine, graph);

        assertThat(navigation.findShortestDistance(논현역, 서울숲))
                .isEqualTo(Distance.of(16));
    }

    @Test
    @DisplayName("같은 노선 위에 있는 두 역의 최단 경로를 구할 수 있다.")
    void shortestPath() {
        WeightedMultigraph<Station, DefaultWeightedEdge> jgraphtGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        final var graph = new JgraphtGraph(jgraphtGraph);
        final var navigation = new Navigation(sectionsOfAllLine, graph);

        assertThat(navigation.findShortestPath(반포역, 청담역))
                .containsExactly(반포역, 논현역, 학동역, 강남구청, 청담역);
    }

    @Test
    @DisplayName("다른 노선 위에 있는 두 역의 최단 경로를 구할 수 있다.")
    void shortestPathOnDifferent() {
        WeightedMultigraph<Station, DefaultWeightedEdge> jgraphtGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        final var graph = new JgraphtGraph(jgraphtGraph);
        final var navigation = new Navigation(sectionsOfAllLine, graph);

        assertThat(navigation.findShortestPath(논현역, 서울숲))
                .containsExactly(논현역, 학동역, 강남구청, 압구정로데오, 서울숲);
    }
}