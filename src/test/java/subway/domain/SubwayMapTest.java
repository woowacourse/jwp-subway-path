package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.LineFixture.FIXTURE_LINE_1;
import static subway.domain.LineFixture.FIXTURE_LINE_2;
import static subway.domain.LineFixture.FIXTURE_LINE_3;
import static subway.domain.SectionFixture.LINE1_SECTIONS;
import static subway.domain.SectionFixture.LINE1_SECTION_ST1_ST2;
import static subway.domain.SectionFixture.LINE2_SECTIONS;
import static subway.domain.SectionFixture.LINE3_SECTIONS;
import static subway.domain.SectionFixture.LINE3_SECTION_ST2_ST9;
import static subway.domain.StationFixture.FIXTURE_STATION_1;
import static subway.domain.StationFixture.FIXTURE_STATION_7;
import static subway.domain.StationFixture.FIXTURE_STATION_9;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.exception.EmptyRoutedStationsSearchResult;

class SubwayMapTest {

    // TODO 어떻게 하면 다양한 테스트 케이스를 확인할 수 있을까?
    @DisplayName("출발 역과 도착 역을 전달받아 환승 포함 최단 거리 경로를 나타내는 역 연결 그래프를 반환한다")
    @Test
    void findShortestRoutedStations() {
        // given
        Map<Line, RoutedStations> sectionsByLine = Map.of(
                FIXTURE_LINE_1, RoutedStations.from(LINE1_SECTIONS),
                FIXTURE_LINE_2, RoutedStations.from(LINE2_SECTIONS),
                FIXTURE_LINE_3, RoutedStations.from(LINE3_SECTIONS)
        );
        SubwayMap subwayMap = new SubwayMap(MultiRoutedStations.from(sectionsByLine));

        // when
        RoutedStations shortestRoutedStations = subwayMap.findShortestRoutedStations(FIXTURE_STATION_1,
                FIXTURE_STATION_9);

        // then
        assertThat(shortestRoutedStations.extractSections())
                .contains(LINE1_SECTION_ST1_ST2, LINE3_SECTION_ST2_ST9);
    }

    @DisplayName("출발 역이 존재하지 않으면 예외를 발생한다")
    @Test
    void findShortestRoutedStationsFailNotExistingSource() {
        // given
        Map<Line, RoutedStations> sectionsByLine = Map.of(FIXTURE_LINE_1, RoutedStations.from(LINE1_SECTIONS));
        SubwayMap subwayMap = new SubwayMap(MultiRoutedStations.from(sectionsByLine));

        // when, then
        assertThatThrownBy(
                () -> subwayMap.findShortestRoutedStations(FIXTURE_STATION_7, FIXTURE_STATION_1))
                .isInstanceOf(EmptyRoutedStationsSearchResult.class)
                .hasMessageContaining("지하철 노선도에 출발 역이 존재하지 않습니다.");
    }

    @DisplayName("도착 역이 존재하지 않으면 예외를 발생한다")
    @Test
    void findShortestRoutedStationsFailNotExistingTarget() {
        // given
        Map<Line, RoutedStations> sectionsByLine = Map.of(FIXTURE_LINE_1, RoutedStations.from(LINE1_SECTIONS));
        SubwayMap subwayMap = new SubwayMap(MultiRoutedStations.from(sectionsByLine));

        // when, then
        assertThatThrownBy(
                () -> subwayMap.findShortestRoutedStations(FIXTURE_STATION_1, FIXTURE_STATION_7))
                .isInstanceOf(EmptyRoutedStationsSearchResult.class)
                .hasMessageContaining("지하철 노선도에 도착 역이 존재하지 않습니다.");
    }
}
