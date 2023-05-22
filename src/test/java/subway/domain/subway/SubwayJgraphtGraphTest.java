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
import subway.domain.section.PathSection;
import subway.exception.InvalidStationException;

class SubwayJgraphtGraphTest {

    @Nested
    @DisplayName("findShortestPathSections 메서드는 ")
    class GetShortestPath {

        @Test
        @DisplayName("호선 정보를 통해 최단 경로를 반환한다.")
        void findShortestPathSections() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            final Line lineOfThree = new Line(3L, "3호선", "주황색", 800);
            final Line lineOfNine = new Line(9L, "9호선", "빨간색", 1000);
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);
            lineOfThree.addSection(GYODAE, NAMBU, 5);
            lineOfThree.addSection(NAMBU, YANGJAE, 5);
            lineOfNine.addSection(GANGNAM, YANGJAE, 5);

            final SubwayJgraphtGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo, lineOfThree, lineOfNine));
            final List<PathSection> result = subwayGraph.findShortestPathSections(GYODAE, GANGNAM);

            final List<PathSection> expected = List.of(
                    new PathSection(3L, GYODAE, NAMBU, 5, 800),
                    new PathSection(3L, NAMBU, YANGJAE, 5, 800),
                    new PathSection(9L, YANGJAE, GANGNAM, 5, 1000)
            );
            assertThat(result).usingRecursiveComparison().isEqualTo(expected);
        }

        @Test
        @DisplayName("출발역이 등록되어 있지 않은 경우 예외를 던진다.")
        void findShortestPathSectionsWithInvalidStartStation() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo));

            assertThatThrownBy(() -> subwayGraph.findShortestPathSections(JAMSIL, GANGNAM))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }

        @Test
        @DisplayName("도착역이 등록되어 있지 않은 경우 예외를 던진다.")
        void findShortestPathSectionsWithInvalidEndStation() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo));

            assertThatThrownBy(() -> subwayGraph.findShortestPathSections(GANGNAM, JAMSIL))
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
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            final Line lineOfThree = new Line(3L, "3호선", "주황색", 800);
            final Line lineOfNew = new Line(9L, "9호선", "빨간색", 1000);
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
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo));

            assertThatThrownBy(() -> subwayGraph.calculateShortestDistance(JAMSIL, GANGNAM))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }

        @Test
        @DisplayName("도착역이 등록되어 있지 않은 경우 예외를 던진다.")
        void getShortestDistanceWithInvalidEndStation() {
            final Line lineOfTwo = new Line(2L, "2호선", "초록색", 500);
            lineOfTwo.addSection(GYODAE, GANGNAM, 20);

            final SubwayGraph subwayGraph = new SubwayJgraphtGraph(List.of(lineOfTwo));

            assertThatThrownBy(() -> subwayGraph.calculateShortestDistance(GANGNAM, JAMSIL))
                    .isInstanceOf(InvalidStationException.class)
                    .hasMessage("노선 구간에 등록되지 않은 역 이름을 통해 경로를 조회할 수 없습니다.");
        }
    }
}
