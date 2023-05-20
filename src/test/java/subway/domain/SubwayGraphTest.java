package subway.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static subway.fixture.SectionFixture.*;
import static subway.fixture.SectionsFixture.SECTIONS;
import static subway.fixture.StationFixture.*;

class SubwayGraphTest {
    static Subway subway;
    static SubwayGraph subwayGraph;
    /*
     용산 -> 이촌 -> 서빙고 -> 한남 -> 옥수 -> 응봉 -> 왕십리
                                  ⬇️                ↘️
                                  압구정                한양대
                                  ⬇️                    ⬇️
                                  신사                    성수
                                  ⬇️                     ⬇️
                                  잠원                    건대입구
                                  ⬇️                        ⬇️
                                  고속터미널                    잠실
                                  ⬇️                        ↗️
                                  교대 -> 강남 -> 선릉 -> 종합운동장
     */

    @BeforeAll
    static void setUp() {
        subway = Subway.from(SECTIONS);
        subwayGraph = SubwayGraph.from(subway);

    }

    @DisplayName("한 노선에서 최단 경로를 구한다 (이촌 - 응봉)")
    @Test
    void getShortestPathInOneLine() {
        ShortestPath shortestPath = subwayGraph.getDijkstraShortestPath(STATION_2, STATION_6);

        assertThat(shortestPath.getPath()).isEqualTo(new ArrayList<>(List.of(SECTION_2, SECTION_3, SECTION_4, SECTION_5)));
        assertThat(shortestPath.getDistance().getDistance()).isEqualTo(35);
        assertThat(shortestPath.getFare()).isEqualTo(1750);
    }

    /*
     이촌 -> 왕십리 -> 잠실 ( 36 + 31 )
     이촌 -> 옥수 -> 교대 -> 잠실 ( 25 + 50 + 20)
     */
    @DisplayName("여러 노선을 고려해 최단 경로를 구한다 (이촌 - 잠실)")
    @Test
    void getShortestPathInMultiLines() {
        ShortestPath shortestPath = subwayGraph.getDijkstraShortestPath(STATION_2, STATION_19);

        assertThat(shortestPath.getPath()).isEqualTo(new ArrayList<>(
                List.of(SECTION_2, SECTION_3, SECTION_4, SECTION_5, SECTION_6, SECTION_19, SECTION_18, SECTION_17, SECTION_16)));
        assertThat(shortestPath.getDistance().getDistance()).isEqualTo(67);
        assertThat(shortestPath.getFare()).isEqualTo(2350);
    }

    @DisplayName("최단경로가 존재하지 않을 때 예외를 발생한다")
    @Test
    void notExistShortestPath() {
        Sections sections = new Sections(new ArrayList<>(List.of(SECTION_1, SECTION_2, SECTION_7)));
        subway = Subway.from(sections);
        subwayGraph = SubwayGraph.from(subway);

        assertThatThrownBy(() -> subwayGraph.getDijkstraShortestPath(STATION_1, STATION_8))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("최단 경로를 찾을 수 없습니다");
    }
}
