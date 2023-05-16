package subway.domain.calculator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.Sections;
import subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class RemoveCalculatorTest {

    @Test
    @DisplayName("조회한 노선에 1개의 구간만 있다면 (존재하는 역이 2개 뿐이라면) 노선 자체를 삭제한다.")
    void removeCalculateTest_whenOnlyTwoStationsExist() {
        // given
        Station stationToDelete = STATION_잠실역;
        RemoveCalculator removeCalculator = new RemoveCalculator(new Sections(List.of(SECTION_잠실역_TO_건대역)));
        Changes expectChanges = new Changes(LINE2_ID,
                new ArrayList<>(), List.of(LINE2),
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>());

        // when
        Changes changes = removeCalculator.calculate(stationToDelete);

        // then
        assertThat(changes).isEqualTo(expectChanges);
    }

    @Test
    @DisplayName("삭제할 역이 상행종점인 경우")
    void removeCalculateTest_whenRemoveUpEndStation() {
        // given
        Station stationToDelete = STATION_잠실역;
        RemoveCalculator removeCalculator = new RemoveCalculator(new Sections(List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역)));
        Changes expectChanges = new Changes(LINE2_ID,
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), List.of(STATION_잠실역),
                new ArrayList<>(), new ArrayList<>());

        // when
        Changes changes = removeCalculator.calculate(stationToDelete);

        // then
        assertThat(changes).isEqualTo(expectChanges);
    }

    @Test
    @DisplayName("삭제할 역이 하행종점인 경우")
    void removeCalculateTest_whenRemoveDownEndStation() {
        // given
        Station stationToDelete = STATION_건대역;
        RemoveCalculator removeCalculator = new RemoveCalculator(new Sections(List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역)));
        Changes expectChanges = new Changes(LINE2_ID,
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), List.of(STATION_건대역),
                new ArrayList<>(), new ArrayList<>());

        // when
        Changes changes = removeCalculator.calculate(stationToDelete);

        // then
        assertThat(changes).isEqualTo(expectChanges);
    }

    @Test
    @DisplayName("삭제할 역이 가운데에 있다면 역을 재정렬해야 한다.")
    void removeCalculateTest_whenRemoveMiddleStation() {
        // given
        Station stationToDelete = STATION_강변역;
        RemoveCalculator removeCalculator = new RemoveCalculator(new Sections(List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역)));
        Changes expectChanges = new Changes(LINE2_ID,
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), List.of(STATION_강변역),
                List.of(SECTION_TO_INSERT_AFTER_DELETE_잠실역_TO_건대역), new ArrayList<>());

        // when
        Changes changes = removeCalculator.calculate(stationToDelete);

        // then
        assertThat(changes).isEqualTo(expectChanges);
    }
}