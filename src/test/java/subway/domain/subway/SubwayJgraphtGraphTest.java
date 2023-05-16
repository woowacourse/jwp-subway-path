package subway.domain.subway;

import static fixtures.StationFixtures.GANGNAM;
import static fixtures.StationFixtures.GYODAE;
import static fixtures.StationFixtures.JAMSIL;
import static fixtures.StationFixtures.NAMBU;
import static fixtures.StationFixtures.YANGJAE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.exception.InvalidStationException;

class SubwayJgraphtGraphTest {

    @Nested
    @DisplayName("findShortestPath 메서드는 ")
    class GetShortestPath {

        @Test
        @DisplayName("호선 정보를 통해 최단 경로를 반환한다.")
        void getShortestPath() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            final Line lineOfThree = new Line(3L, "3호선", "주황색");
            final Line lineOfNew = new Line(9L, "9호선", "빨간색");
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);
            lineOfThree.addSection(GYODAE, NAMBU, 5);
            lineOfThree.addSection(NAMBU, YANGJAE, 5);
            lineOfNew.addSection(GANGNAM, YANGJAE, 5);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo, lineOfThree, lineOfNew));
            final List<Station> result = subwayGraph.findShortestPath(GYODAE, GANGNAM);

            assertThat(result).containsExactly(GYODAE, NAMBU, YANGJAE, GANGNAM);
        }

        @Test
        @DisplayName("출발역이 등록되어 있지 않은 경우 예외를 던진다.")
        void getShortestPathWithInvalidStartStation() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo));

            assertThatThrownBy(() -> subwayGraph.findShortestPath(JAMSIL, GANGNAM))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }

        @Test
        @DisplayName("도착역이 등록되어 있지 않은 경우 예외를 던진다.")
        void getShortestPathWithInvalidEndStation() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo));

            assertThatThrownBy(() -> subwayGraph.findShortestPath(GANGNAM, JAMSIL))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("calculateShortestDistance 메서드는 ")
    class GetShortestDistance {

        @Test
        @DisplayName("호선 정보를 통해 최단 거리를 반환한다.")
        void getShortestDistance() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            final Line lineOfThree = new Line(3L, "3호선", "주황색");
            final Line lineOfNew = new Line(9L, "9호선", "빨간색");
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);
            lineOfThree.addSection(GYODAE, NAMBU, 5);
            lineOfThree.addSection(NAMBU, YANGJAE, 5);
            lineOfNew.addSection(GANGNAM, YANGJAE, 5);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo, lineOfThree, lineOfNew));
            final long result = subwayGraph.calculateShortestDistance(GYODAE, GANGNAM);

            assertThat(result).isEqualTo(15);
        }

        @Test
        @DisplayName("출발역이 등록되어 있지 않은 경우 예외를 던진다.")
        void getShortestDistanceWithInvalidStartStation() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo));

            assertThatThrownBy(() -> subwayGraph.calculateShortestDistance(JAMSIL, GANGNAM))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }

        @Test
        @DisplayName("도착역이 등록되어 있지 않은 경우 예외를 던진다.")
        void getShortestDistanceWithInvalidEndStation() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo));

            assertThatThrownBy(() -> subwayGraph.calculateShortestDistance(GANGNAM, JAMSIL))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }
    }
}
