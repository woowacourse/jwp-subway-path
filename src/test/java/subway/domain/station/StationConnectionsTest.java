package subway.domain.station;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.exception.StationNotFoundException;

import java.util.List;

import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StationConnectionsTest {

    @Test
    @DisplayName("상행역과 하행역을 순서에 맞게 정렬하여 역 이름 리스트를 반환한다.")
    void sortStationsTest() {
        // given
        List<Section> sections = List.of(
                SECTION_강변역_TO_건대역,
                SECTION_대림역_TO_잠실역,
                SECTION_잠실역_TO_강변역);
        StationConnections stationConnections = StationConnections.fromSections(sections);

        List<String> expectSortedStationNames = List.of(
                STATION_대림역_NAME, STATION_잠실역_NAME,
                STATION_강변역_NAME, STATION_건대역_NAME);

        // when
        List<String> sortedStationNames = stationConnections.getSortedStationNames();

        // then
        assertThat(sortedStationNames).isEqualTo(expectSortedStationNames);
    }

    @Test
    @DisplayName("섹션 정보가 없을 때 상행 종점을 찾을 수 없으므로 예왹가 발생한다.")
    void cannotFindUpStationTest() {
        // given
        StationConnections stationConnections = StationConnections.fromSections(List.of());

        // when, then
        assertThatThrownBy(() -> stationConnections.getSortedStationNames())
                .isInstanceOf(StationNotFoundException.class)
                .hasMessage("상행 종점을 찾을 수 없습니다.");
    }
}