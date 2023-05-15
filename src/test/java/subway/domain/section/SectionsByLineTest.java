package subway.domain.section;

import static fixtures.LineFixtures.INITIAL_Line2;
import static fixtures.LineFixtures.Line7;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.station.Station;

class SectionsByLineTest {

    @Test
    @DisplayName("모든 노선별 순서대로 정렬된 역 이름 맵을 반환한다.")
    void getAllSortedStationNamesByLine() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        Line line7 = Line7.DUMMY_LINE7;
        Station line2StationA = INITIAL_STATION_A.FIND_STATION;
        Station line2StationB = STATION_B.createDummyStation(-1L, line2);
        Section sectionA_TO_B_Line2 = new Section(-1L, NearbyStations.createByUpStationAndDownStation(
                line2StationA, line2StationB), line2, new Distance(5));

        Station line7StationB = STATION_B.createDummyStation(-1L, line7);
        Station line7StationD = STATION_D.createDummyStation(-2L, line7);
        Section sectionB_TO_D_Line7 = new Section(-2L, NearbyStations.createByUpStationAndDownStation(
                line7StationB, line7StationD), line7, new Distance(10));

        Sections line2Sections = new Sections(List.of(sectionA_TO_B_Line2));
        Sections line7Sections = new Sections(List.of(sectionB_TO_D_Line7));
        Map<Line, Sections> sectionsByLine = new HashMap<>();
        sectionsByLine.put(line2, line2Sections);
        sectionsByLine.put(line7, line7Sections);

        SectionsByLine generatedSectionsByLine = new SectionsByLine(sectionsByLine);
        // when
        Map<Line, List<String>> allSortedStationNamesByLine =
                generatedSectionsByLine.getAllSortedStationNamesByLine();

        // then
        assertAll(
                () -> assertThat(allSortedStationNamesByLine.get(line2)).containsExactly(INITIAL_STATION_A.NAME, line2StationB.getName()),
                () -> assertThat(allSortedStationNamesByLine.get(line7)).containsExactly(line7StationB.getName(), line7StationD.getName())
        );
    }
}
