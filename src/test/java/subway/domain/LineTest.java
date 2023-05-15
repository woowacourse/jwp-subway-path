package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class LineTest {


    public static final Station STATION_A = new Station(1L, "A");
    public static final Station STATION_B = new Station(2L, "B");
    public static final Station STATION_C = new Station(3L, "C");
    public static final Station STATION_D = new Station(4L, "D");

    @DisplayName("라인을 최초 생성한다.")
    @Test
    void createLine() {
        // given
        // when
        Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);

        // then
        assertThat(line.getEdges()).hasSize(1);
    }

    @Nested
    class StationAddToLine {

        @Nested
        class AddSuccess {
            @Test
            void validate1() {
                // given
                Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);
                line.addEdge(STATION_B, STATION_C, 8);

                // when
                line.addEdge(STATION_D, STATION_B, 3);

                // then
                assertThat(line.getStations()).containsExactly(STATION_A, STATION_D, STATION_B, STATION_C);
            }

            @Test
            void validate2() {
                // given
                Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);
                line.addEdge(STATION_B, STATION_C, 8);

                // when
                line.addEdge(STATION_B, STATION_D, 3);

                // then
                assertThat(line.getStations()).containsExactly(STATION_A, STATION_B, STATION_D, STATION_C);
            }

            @Test
            void validate3() {
                // given
                Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);

                // when
                line.addEdge(STATION_C, STATION_A, 3);

                // then
                assertThat(line.getStations()).containsExactly(STATION_C, STATION_A, STATION_B);
            }

            @Test
            void validate4() {
                // given
                Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);

                // when
                line.addEdge(STATION_A, STATION_C, 3);

                // then
                assertThat(line.getStations()).containsExactly(STATION_A, STATION_C, STATION_B);
            }

            @Test
            void validate5() {
                // given
                Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);

                // when
                line.addEdge(STATION_C, STATION_B, 3);

                // then
                assertThat(line.getStations()).containsExactly(STATION_A, STATION_C, STATION_B);
            }

            @Test
            void validate6() {
                // given
                Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);

                // when
                line.addEdge(STATION_B, STATION_C, 3);

                // then
                assertThat(line.getStations()).containsExactly(STATION_A, STATION_B, STATION_C);
            }
        }

        @Nested
        class AddFail {

        }
    }

    @Nested
    class DeleteStationFromLine {

        @Test
        void deleteStation1() {
            // given
            Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);
            line.addEdge(STATION_B, STATION_C, 3);

            // when
            line.deleteStation(STATION_A);

            // then
            assertThat(line.getStations()).containsExactly(STATION_B, STATION_C);
        }

        @Test
        void deleteStation2() {
            // given
            Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);
            line.addEdge(STATION_B, STATION_C, 3);

            // when
            line.deleteStation(STATION_B);

            // then
            assertThat(line.getStations()).containsExactly(STATION_A, STATION_C);
        }

        @Test
        void deleteStation3() {
            // given
            Line line = Line.createLine("2호선", STATION_A, STATION_B, 10);
            line.addEdge(STATION_B, STATION_C, 3);

            // when
            line.deleteStation(STATION_C);

            // then
            assertThat(line.getStations()).containsExactly(STATION_A, STATION_B);
        }

        @Nested
        class AddFail {

        }
    }

    @Test
    @DisplayName("해당 노선에 존재하는 모든 역을 가져온다.")
    void blah() {
        Station a = new Station(1L, "A");
        Station b = new Station(2L, "B");
        Station c = new Station(3L, "C");
        Station d = new Station(4L, "D");

        List<Edge> edges = List.of(new Edge(c, d, 5), new Edge(a, b, 3), new Edge(b, c, 4));
        Line line = new Line(1L, "3호선", edges);

        List<Station> allStation = line.getStations();
        assertThat(allStation).containsExactly(a, b, c, d);
    }
}
