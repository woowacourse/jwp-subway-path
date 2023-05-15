package subway.domain.graph;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.edge.Distance;
import subway.domain.edge.Edge;
import subway.domain.edge.Edges;
import subway.domain.line.Line;
import subway.domain.station.Station;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayGraphTest {

    private static final Station A = new Station(1L, "A");
    private static final Station B = new Station(2L, "B");
    private static final Station C = new Station(3L, "C");
    private static final Station D = new Station(4L, "D");
    private static final Station E = new Station(5L, "E");
    private static final Station F = new Station(6L, "F");
    private static final Station G = new Station(7L, "G");
    private static final Station H = new Station(8L, "H");

    // given
    final Map<Line, Edges> allEdges = Map.of(
            new Line(1L, "1호선"), new Edges(
                    List.of(
                            new Edge(1L, A, B, new Distance(3)),
                            new Edge(2L, B, C, new Distance(4)),
                            new Edge(3L, C, D, new Distance(5)),
                            new Edge(4L, D, E, new Distance(5))

                    )
            ),
            new Line(2L, "2호선"), new Edges(
                    List.of(
                            new Edge(5L, B, C, new Distance(3)),
                            new Edge(6L, C, D, new Distance(1)),
                            new Edge(7L, D, H, new Distance(5))
                    )
            )
    );
    final DijkstraShortestPath<Station, Edge> shortestPath = SubwayGraph.getShortestPath(allEdges);


    @DisplayName("A에서 H로 간다.")
    @Test
    void getShortestPath1() {
        // when
        final double pathWeight = shortestPath.getPathWeight(A, H);
        final List<Station> stations = shortestPath.getPath(A, H).getVertexList();

        // then
        assertThat(pathWeight).isEqualTo(12);
        assertThat(stations).containsExactly(A, B, C, D, H);
    }

    @DisplayName("B에서 D로 간다.")
    @Test
    void getShortestPath2() {
        // when
        final double pathWeight = shortestPath.getPathWeight(B, D);
        final List<Station> stations = shortestPath.getPath(B, D).getVertexList();

        // then
        assertThat(pathWeight).isEqualTo(4);
        assertThat(stations).containsExactly(B, C, D);
    }

    @DisplayName("B에서 E로 간다.")
    @Test
    void getShortestPath3() {
        // when
        final double pathWeight = shortestPath.getPathWeight(B, E);
        final List<Station> stations = shortestPath.getPath(B, E).getVertexList();

        // then
        assertThat(pathWeight).isEqualTo(9);
        assertThat(stations).containsExactly(B, C, D, E);
    }

    @DisplayName("C에서 D로 간다.")
    @Test
    void getShortestPath4() {
        // when
        final double pathWeight = shortestPath.getPathWeight(C, D);
        final List<Station> stations = shortestPath.getPath(C, D).getVertexList();

        // then
        assertThat(pathWeight).isEqualTo(1);
        assertThat(stations).containsExactly(C, D);
    }

    @DisplayName("H에서 A로 간다.")
    @Test
    void getShortestPath5() {
        // when
        final double pathWeight = shortestPath.getPathWeight(H, A);
        final List<Station> stations = shortestPath.getPath(H, A).getVertexList();

        // then
        assertThat(pathWeight).isEqualTo(12);
        assertThat(stations).containsExactly(H, D, C, B, A);
    }

    @DisplayName("E에서 B로 간다.")
    @Test
    void getShortestPath6() {
        // when
        final double pathWeight = shortestPath.getPathWeight(E, B);
        final List<Station> stations = shortestPath.getPath(E, B).getVertexList();

        // then
        assertThat(pathWeight).isEqualTo(9);
        assertThat(stations).containsExactly(E, D, C, B);
    }
}
