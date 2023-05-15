package subway.domain.subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.exception.InvalidStationNameException;

class SubwayTest {

    @Nested
    @DisplayName("getShortestPath 메서드는 ")
    class GetShortestPath {

        @Test
        @DisplayName("지하철 상의 최단 경로를 반환한다.")
        void getShortestPath() {
            final Station gangnam = new Station(1L, "강남역");
            final Station yangjae = new Station(2L, "양재역");
            final Station gyodae = new Station(3L, "교대역");
            final Station nambu = new Station(4L, "남부터미널역");
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            final Line lineOfThree = new Line(3L, "3호선", "주황색");
            final Line lineOfNew = new Line(9L, "9호선", "빨간색");
            lineOfTwo.addSection(gyodae, gangnam, 20);
            lineOfThree.addSection(gyodae, nambu, 5);
            lineOfThree.addSection(nambu, yangjae, 5);
            lineOfNew.addSection(gangnam, yangjae, 5);

            final Subway subway = new Subway(List.of(lineOfTwo, lineOfThree, lineOfNew));
            final List<String> result = subway.getShortestPath(gyodae.getName(), gangnam.getName());

            assertThat(result).containsExactly("교대역", "남부터미널역", "양재역", "강남역");
        }

        @Test
        @DisplayName("출발역이 지하철에 등록되어 있지 않은 경우 예외를 던진다.")
        void getShortestPathWithInvalidStartStation() {
            final Station gangnam = new Station(1L, "강남역");
            final Station gyodae = new Station(2L, "교대역");
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            lineOfTwo.addSection(gyodae, gangnam, 20);

            final Subway subway = new Subway(List.of(lineOfTwo));

            assertThatThrownBy(() -> subway.getShortestPath("잠실역", gangnam.getName()))
                    .isInstanceOf(InvalidStationNameException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }

        @Test
        @DisplayName("도착역이 지하철에 등록되어 있지 않은 경우 예외를 던진다.")
        void getShortestPathWithInvalidEndStation() {
            final Station gangnam = new Station(1L, "강남역");
            final Station gyodae = new Station(2L, "교대역");
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            lineOfTwo.addSection(gyodae, gangnam, 20);

            final Subway subway = new Subway(List.of(lineOfTwo));

            assertThatThrownBy(() -> subway.getShortestPath(gangnam.getName(), "잠실역"))
                    .isInstanceOf(InvalidStationNameException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }
    }

    @Nested
    @DisplayName("getShortestDistance 메서드는 ")
    class GetShortestDistance {

        @Test
        @DisplayName("지하철 상의 최단 거리를 반환한다.")
        void getShortestDistance() {
            final Station gangnam = new Station(1L, "강남역");
            final Station yangjae = new Station(2L, "양재역");
            final Station gyodae = new Station(3L, "교대역");
            final Station nambu = new Station(4L, "남부터미널역");
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            final Line lineOfThree = new Line(3L, "3호선", "주황색");
            final Line lineOfNew = new Line(9L, "9호선", "빨간색");
            lineOfTwo.addSection(gyodae, gangnam, 20);
            lineOfThree.addSection(gyodae, nambu, 5);
            lineOfThree.addSection(nambu, yangjae, 5);
            lineOfNew.addSection(gangnam, yangjae, 5);

            final Subway subway = new Subway(List.of(lineOfTwo, lineOfThree, lineOfNew));
            final int result = subway.getShortestDistance(gyodae.getName(), gangnam.getName());

            assertThat(result).isEqualTo(15);
        }

        @Test
        @DisplayName("출발역이 지하철에 등록되어 있지 않은 경우 예외를 던진다.")
        void getShortestDistanceWithInvalidStartStation() {
            final Station gangnam = new Station(1L, "강남역");
            final Station gyodae = new Station(2L, "교대역");
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            lineOfTwo.addSection(gyodae, gangnam, 20);

            final Subway subway = new Subway(List.of(lineOfTwo));

            assertThatThrownBy(() -> subway.getShortestDistance("잠실역", gangnam.getName()))
                    .isInstanceOf(InvalidStationNameException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }

        @Test
        @DisplayName("도착역이 지하철에 등록되어 있지 않은 경우 예외를 던진다.")
        void getShortestDistanceWithInvalidEndStation() {
            final Station gangnam = new Station(1L, "강남역");
            final Station gyodae = new Station(2L, "교대역");
            final Line lineOfTwo = new Line(2L, "2호선", "초록색");
            lineOfTwo.addSection(gyodae, gangnam, 20);

            final Subway subway = new Subway(List.of(lineOfTwo));

            assertThatThrownBy(() -> subway.getShortestDistance(gangnam.getName(), "잠실역"))
                    .isInstanceOf(InvalidStationNameException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }
    }
}
