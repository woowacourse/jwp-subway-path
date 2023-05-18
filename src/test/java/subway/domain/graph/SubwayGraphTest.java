package subway.domain.graph;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.fare.DistanceFarePolicy;
import subway.domain.line.Line;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.List;

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


    @Nested
    class FindPathTest {

        // given
        final List<Line> lines = List.of(
                new Line(1L, "1호선", new Sections(
                        List.of(
                                new Section(1L, A, B, new Distance(3)),
                                new Section(2L, B, C, new Distance(4)),
                                new Section(3L, C, D, new Distance(5)),
                                new Section(4L, D, E, new Distance(5))

                        )
                )),
                new Line(2L, "2호선", new Sections(
                        List.of(
                                new Section(5L, B, C, new Distance(3)),
                                new Section(6L, C, D, new Distance(1)),
                                new Section(7L, D, H, new Distance(5))
                        )
                ))
        );

        final SubwayGraph graph = SubwayGraph.of(lines, new DistanceFarePolicy());

        @DisplayName("A에서 H로 간다.")
        @Test
        void getShortestPath1() {
            // when
            final double pathWeight = graph.getShortestPathDistance(A, H);
            final List<Station> stations = graph.getShortestPath(A, H);
            final int pathFare = graph.getPathFare(A, H);

            // then
            assertThat(pathWeight).isEqualTo(12);
            assertThat(stations).containsExactly(A, B, C, D, H);
            assertThat(pathFare).isEqualTo(1350);
        }

        @DisplayName("B에서 D로 간다.")
        @Test
        void getShortestPath2() {
            // when
            final double pathWeight = graph.getShortestPathDistance(B, D);
            final List<Station> stations = graph.getShortestPath(B, D);
            final int pathFare = graph.getPathFare(B, D);

            // then
            assertThat(pathWeight).isEqualTo(4);
            assertThat(stations).containsExactly(B, C, D);
            assertThat(pathFare).isEqualTo(1250);
        }

        @DisplayName("B에서 E로 간다.")
        @Test
        void getShortestPath3() {
            // when
            final double pathWeight = graph.getShortestPathDistance(B, E);
            final List<Station> stations = graph.getShortestPath(B, E);
            final int pathFare = graph.getPathFare(B, E);

            // then
            assertThat(pathWeight).isEqualTo(9);
            assertThat(stations).containsExactly(B, C, D, E);
            assertThat(pathFare).isEqualTo(1250);
        }

        @DisplayName("C에서 D로 간다.")
        @Test
        void getShortestPath4() {
            // when
            final double pathWeight = graph.getShortestPathDistance(C, D);
            final List<Station> stations = graph.getShortestPath(C, D);
            final int pathFare = graph.getPathFare(C, D);

            // then
            assertThat(pathWeight).isEqualTo(1);
            assertThat(stations).containsExactly(C, D);
            assertThat(pathFare).isEqualTo(1250);
        }

        @DisplayName("H에서 A로 간다.")
        @Test
        void getShortestPath5() {
            // when
            final double pathWeight = graph.getShortestPathDistance(H, A);
            final List<Station> stations = graph.getShortestPath(H, A);
            final int pathFare = graph.getPathFare(H, A);

            // then
            assertThat(pathWeight).isEqualTo(12);
            assertThat(stations).containsExactly(H, D, C, B, A);
            assertThat(pathFare).isEqualTo(1350);
        }

        @DisplayName("E에서 B로 간다.")
        @Test
        void getShortestPath6() {
            // when
            final double pathWeight = graph.getShortestPathDistance(E, B);
            final List<Station> stations = graph.getShortestPath(E, B);
            final int pathFare = graph.getPathFare(E, B);

            // then
            assertThat(pathWeight).isEqualTo(9);
            assertThat(stations).containsExactly(E, D, C, B);
            assertThat(pathFare).isEqualTo(1250);
        }
    }

    @Nested
    class FareTest {
        // given
        final List<Line> lines = List.of(
                new Line(1L, "1호선", new Sections(
                        List.of(
                                new Section(1L, A, B, new Distance(18)),
                                new Section(2L, B, C, new Distance(19)),
                                new Section(3L, C, D, new Distance(20)),
                                new Section(4L, D, E, new Distance(20))

                        )
                )),
                new Line(2L, "2호선", new Sections(
                        List.of(
                                new Section(5L, B, C, new Distance(18)),
                                new Section(6L, C, D, new Distance(16)),
                                new Section(7L, D, H, new Distance(20))
                        )
                ))
        );

        final SubwayGraph graph = SubwayGraph.of(lines, new DistanceFarePolicy());

        @Test
        void fare1() {
            // when
            final double distance = graph.getShortestPathDistance(A, H);
            final int pathFare = graph.getPathFare(A, H);

            // then
            assertThat(distance).isEqualTo(72);
            assertThat(pathFare).isEqualTo(2250);
        }

        @Test
        void fare2() {
            // when


            // then
        }
    }
}
