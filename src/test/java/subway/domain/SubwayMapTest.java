package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노션 관련 기능")
class SubwayMapTest {

    private final SubwayMap subwayMap = new SubwayMap();

    @DisplayName("지하철 노선에 역을 추가한다.")
    @Test
    void addSection() {
        //given
        Section section = new Section(1L, 1L, 1L, 2L, 1);

        //when
        subwayMap.addSection(section);
        //then
        boolean result = subwayMap.containsSection(1L, 2L);

        assertThat(result).isTrue();
    }

    @DisplayName("시작역 id와 도착역 id를 입력하면 해당하는 가장 빠른 경로에 해당하는 역들의 id를 반환한다.")
    @Test
    void findShortestPathIds() {
        //given
        Section section1 = new Section(1L, 1L, 1L, 2L, 1);
        Section section2 = new Section(2L, 1L, 2L, 3L, 2);
        Section section3 = new Section(3L, 1L, 3L, 4L, 3);
        Section section4 = new Section(4L, 1L, 4L, 5L, 4);

        subwayMap.addSection(section1);
        subwayMap.addSection(section2);
        subwayMap.addSection(section3);
        subwayMap.addSection(section4);

        //when
        List<Long> shortestPathIds = subwayMap.findShortestPathIds(1L, 5L);

        //then
        assertThat(shortestPathIds).containsExactly(1L, 2L, 3L, 4L, 5L);
    }

    @DisplayName("최단 경로의 거리를 반환한다.")
    @Test
    void getTotalDistance() {
        //given
        Section section1 = new Section(1L, 1L, 1L, 2L, 1);
        Section section2 = new Section(2L, 1L, 2L, 3L, 2);
        Section section3 = new Section(3L, 1L, 3L, 4L, 3);
        Section section4 = new Section(4L, 1L, 4L, 5L, 4);

        subwayMap.addSection(section1);
        subwayMap.addSection(section2);
        subwayMap.addSection(section3);
        subwayMap.addSection(section4);

        //when
        Double totalDistance = subwayMap.getTotalDistance(1L, 5L);

        //then
        assertThat(totalDistance).isEqualTo(10);
    }
}