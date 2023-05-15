package subway.domain.section;

import static fixtures.LineFixtures.INITIAL_Line2;
import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.exception.SectionNotFoundException;

class SectionsTest {

    @Test
    @DisplayName("해당 역을 하행역으로 가지는 구간을 반환한다.")
    void findSectionByDownStation() {
        // given
        Station stationC = INITIAL_STATION_C.FIND_STATION;
        Section sectionAtoC = INITIAL_SECTION_A_TO_C.FIND_SECTION;
        Sections sections = new Sections(List.of(sectionAtoC));

        // when
        Section findSection = sections.findSectionByDownStation(stationC);

        // then
        assertThat(findSection).usingRecursiveComparison().isEqualTo(sectionAtoC);
    }

    @Test
    @DisplayName("해당 역을 하행역으로 가지는 구간을 조회할 때, 해당하는 구간이 없으면 예외가 발생한다.")
    void findSectionByDownStation_throw_not_found() {
        // given
        Station stationE = STATION_E.createStationToInsert(INITIAL_Line2.FIND_LINE);
        Section sectionAtoC = INITIAL_SECTION_A_TO_C.FIND_SECTION;
        Sections sections = new Sections(List.of(sectionAtoC));

        // when, then
        assertThatThrownBy(() -> sections.findSectionByDownStation(stationE))
                .isInstanceOf(SectionNotFoundException.class)
                .hasMessage("해당 역을 하행역으로 가지는 구간이 없습니다.");
    }

    @Test
    @DisplayName("해당 역을 상행역으로 가지는 구간을 반환한다.")
    void findSectionByUpStation() {
        // given
        Station stationA = INITIAL_STATION_A.FIND_STATION;
        Section sectionAtoC = INITIAL_SECTION_A_TO_C.FIND_SECTION;
        Sections sections = new Sections(List.of(sectionAtoC));

        // when
        Section findSection = sections.findSectionByUpStation(stationA);

        // then
        assertThat(findSection).usingRecursiveComparison().isEqualTo(sectionAtoC);
    }

    @Test
    @DisplayName("해당 역을 상행역으로 가지는 구간을 조회할 때, 해당하는 구간이 없으면 예외가 발생한다.")
    void findSectionByUpStation_throw_not_found() {
        // given
        Station stationE = STATION_E.createStationToInsert(INITIAL_Line2.FIND_LINE);
        Section sectionAtoC = INITIAL_SECTION_A_TO_C.FIND_SECTION;
        Sections sections = new Sections(List.of(sectionAtoC));

        // when, then
        assertThatThrownBy(() -> sections.findSectionByUpStation(stationE))
                .isInstanceOf(SectionNotFoundException.class)
                .hasMessage("해당 역을 상행역으로 가지는 구간이 없습니다.");
    }

    @Test
    @DisplayName("역 ID에 해당하는 역으로 조회한 상행 구간, 하행 구간 중 하나만 존재하면 END_SECTION 케이스를 반환한다.")
    void determineSectionCaseByStationId_END_SECTION() {
        // given
        Section initSectionAtoC = INITIAL_SECTION_A_TO_C.FIND_SECTION;
        Station stationD = STATION_D.createDummyStation(100L, INITIAL_Line2.FIND_LINE);
        Section sectionDtoA = SECTION_D_TO_A.createSectionToInsert(
                stationD, INITIAL_STATION_A.FIND_STATION, INITIAL_Line2.FIND_LINE
        );
        Sections sections = new Sections(List.of(initSectionAtoC, sectionDtoA));
        Long stationDId = stationD.getId();

        // when, then
        assertThat(sections.determineSectionCaseByStationId(stationDId))
                .isEqualTo(SectionCase.END_SECTION);
    }

    @Test
    @DisplayName("역 ID에 해당하는 역으로 조회한 상행 구간, 하행 구간 모두 존재하면 MIDDLE_SECTION 케이스를 반환한다.")
    void determineSectionCaseByStationId_MIDDLE_SECTION() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        Station stationA = INITIAL_STATION_A.FIND_STATION;
        Station stationB = STATION_B.createDummyStation(100L, line2);
        Station stationC = INITIAL_STATION_C.FIND_STATION;
        Section sectionAtoB = SECTION_A_TO_B.createSectionToInsert(stationA, stationB, line2);
        Section sectionBtoC = SECTION_B_TO_C.createSectionToInsert(stationB, stationC, line2);
        Sections sections = new Sections(List.of(sectionAtoB, sectionBtoC));

        Long stationBId = stationB.getId();

        // when, then
        assertThat(sections.determineSectionCaseByStationId(stationBId))
                .isEqualTo(SectionCase.MIDDLE_SECTION);
    }

    @Test
    @DisplayName("하행역 이름에 해당하는 역을 하행역으로 가지고 노선에 해당하는 구간을 반환한다.")
    void findSectionHasDownStationNameAsDownStationByLine() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        String stationCName = INITIAL_STATION_C.NAME;
        Section sectionAtoC = INITIAL_SECTION_A_TO_C.FIND_SECTION;
        Sections sections = new Sections(List.of(sectionAtoC));

        // when
        Optional<Section> currentSection =
                sections.findSectionHasDownStationNameAsDownStationByLine(stationCName, line2);

        // then
        assertThat(currentSection.get()).usingRecursiveComparison().isEqualTo(sectionAtoC);
    }

    @Test
    @DisplayName("하행역 이름에 해당하는 역을 하행역으로 가지고 노선에 해당하는 구간 반환 시 해당 구간이 없으면 빈 Optional을 반환한디.")
    void findSectionHasDownStationNameAsDownStationByLine_EmptyOptional() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        String stationEName = STATION_E.NAME;
        Section sectionAtoC = INITIAL_SECTION_A_TO_C.FIND_SECTION;
        Sections sections = new Sections(List.of(sectionAtoC));

        // when
        Optional<Section> currentSection =
                sections.findSectionHasDownStationNameAsDownStationByLine(stationEName, line2);

        // then
        assertThat(currentSection.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("상행역 이름에 해당하는 역을 상행역으로 가지고 노선에 해당하는 구간을 반환한다.")
    void findSectionHasUpStationNameAsUpStationByLine() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        String stationAName = INITIAL_STATION_A.NAME;
        Section sectionAtoC = INITIAL_SECTION_A_TO_C.FIND_SECTION;
        Sections sections = new Sections(List.of(sectionAtoC));

        // when
        Optional<Section> currentSection =
                sections.findSectionHasUpStationNameAsUpStationByLine(stationAName, line2);

        // then
        assertThat(currentSection.get()).usingRecursiveComparison().isEqualTo(sectionAtoC);
    }

    @Test
    @DisplayName("상행역 이름에 해당하는 역을 상행역으로 가지고 노선에 해당하는 구간 반환 시 해당 구간이 없으면 빈 Optional을 반환한디.")
    void findSectionHasUpStationNameAsUpStationByLine_EmptyOptional() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        String stationEName = STATION_E.NAME;
        Section sectionAtoC = INITIAL_SECTION_A_TO_C.FIND_SECTION;
        Sections sections = new Sections(List.of(sectionAtoC));

        // when
        Optional<Section> currentSection =
                sections.findSectionHasUpStationNameAsUpStationByLine(stationEName, line2);

        // then
        assertThat(currentSection.isEmpty()).isTrue();
    }
}
