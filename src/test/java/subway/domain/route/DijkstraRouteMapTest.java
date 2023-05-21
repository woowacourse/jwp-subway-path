package subway.domain.route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayName("그래프 테스트")
class DijkstraRouteMapTest {
    @Test
    void 모든_노선을_입력받아_그래프를_생성한다() {
        // given
        Section 첫번째_구간 = Section.of(new Station(1L, "잠실역"), new Station(2L, "잠실새내역"), 10);
        Section 두번째_구간 = Section.of(new Station(2L, "잠실새내역"), new Station(3L, "종합운동장역"), 15);
        Line 이호선 = Line.of(1L, "2호선", List.of(첫번째_구간, 두번째_구간));

        Section 세번째_구간 = Section.of(new Station(4L, "독산역"), new Station(5L, "가산디지털단지역"), 10);
        Section 네번째_구간 = Section.of(new Station(5L, "가산디지털단지역"), new Station(6L, "구로역"), 7);
        Line 일호선 = Line.of(1L, "2호선", List.of(세번째_구간, 네번째_구간));

        // then
        assertDoesNotThrow(() -> new DijkstraRouteMap(일호선, 이호선));
    }

    @Test
    void 환승하지_않는_출발역과_도착역을_입력받아_최단경로를_계산한다() {
        // given
        Station 출발역 = new Station(1L, "낙성대역");
        Section 첫번째_구간 = Section.of(출발역, new Station(2L, "사당역"), 10);
        Section 두번째_구간 = Section.of(new Station(2L, "사당역"), new Station(3L, "방배역"), 15);
        Section 세번째_구간 = Section.of(new Station(3L, "방배역"), new Station(4L, "서초역"), 7);

        Station 도착역 = new Station(5L, "교대역");
        Section 네번째_구간 = Section.of(new Station(4L, "서초역"), 도착역, 5);
        Line 이호선 = Line.of(1L, "2호선", List.of(첫번째_구간, 두번째_구간, 세번째_구간, 네번째_구간));

        // when
        DijkstraRouteMap 노선도 = new DijkstraRouteMap(이호선);
        Path 경로 = 노선도.findShortestPath(출발역, 도착역);

        // then
        assertAll(
                () -> assertThat(경로.getPath()).containsExactly(
                        출발역,
                        new Station(2L, "사당역"),
                        new Station(3L, "방배역"),
                        new Station(4L, "서초역"),
                        도착역
                ),
                () -> assertThat(경로.getShortestDistance()).isEqualTo(37)
        );
    }

    @Test
    void 환승하는_출발역과_도착역을_입력받아_최단경로를_계산한다() {
        // given
        Station 출발역 = new Station(1L, "낙성대역");
        Section 이호선_첫번째_구간 = Section.of(출발역, new Station(2L, "사당역"), 10);
        Section 이호선_두번째_구간 = Section.of(new Station(2L, "사당역"), new Station(3L, "방배역"), 15);
        Section 이호선_세번째_구간 = Section.of(new Station(3L, "방배역"), new Station(4L, "서초역"), 7);
        Section 이호선_네번째_구간 = Section.of(new Station(4L, "서초역"), new Station(5L, "교대역"), 5);
        Line 이호선 = Line.of(1L, "2호선", List.of(이호선_첫번째_구간, 이호선_두번째_구간, 이호선_세번째_구간, 이호선_네번째_구간));

        Section 사호선_첫번째_구간 = Section.of(new Station(2L, "사당역"), new Station(6L, "총신대입구역"), 8);

        Station 도착역 = new Station(7L, "동작역");
        Section 사호선_두번째_구간 = Section.of(new Station(6L, "총신대입구역"), 도착역, 2);

        Line 사호선 = Line.of(2L, "4호선", List.of(사호선_첫번째_구간, 사호선_두번째_구간));

        // when
        DijkstraRouteMap 노선도 = new DijkstraRouteMap(이호선, 사호선);
        Path 경로 = 노선도.findShortestPath(출발역, 도착역);

        // then
        assertAll(
                () -> assertThat(경로.getPath()).containsExactly(
                        출발역,
                        new Station(2L, "사당역"),
                        new Station(6L, "총신대입구역"),
                        도착역
                ),
                () -> assertThat(경로.getShortestDistance()).isEqualTo(20)
        );
    }
}
