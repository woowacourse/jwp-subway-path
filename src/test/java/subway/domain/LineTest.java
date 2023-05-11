package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

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
        Line line = Line.createLine("2호선", new Station("잠실"), new Station("잠실나루"), 10);

        // when
        line.addEdge(new Station("강변"), new Station("잠실"), 3);

        // then
        assertThat(line.getEdges()).hasSize(2);
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
        class AddFail{

        }

    }
}
