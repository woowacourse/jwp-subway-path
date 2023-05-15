package subway.domain.section;

import static fixtures.LineFixtures.INITIAL_Line2;
import static fixtures.SectionFixtures.INITIAL_SECTION_A_TO_C;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import fixtures.SectionFixtures.SECTION_A_TO_B;
import fixtures.SectionFixtures.SECTION_B_TO_C;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.line.Line;
import subway.domain.station.Station;

class NewSectionMakerTest {

    @Test
    @DisplayName("기본 구간을 생성한다.")
    void makeSectionToSave() {
        // when
        Section generatedSection = NewSectionMaker.makeSectionToSave(
                INITIAL_Line2.FIND_LINE,
                INITIAL_STATION_A.FIND_STATION,
                INITIAL_STATION_C.FIND_STATION,
                INITIAL_SECTION_A_TO_C.DISTANCE
        );

        // then
        assertThat(generatedSection).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(INITIAL_SECTION_A_TO_C.FIND_SECTION);
    }

    @Test
    @DisplayName("등록할 하행 구간과 기존 구간을 받아서 새로운 상행 구간을 생성한다.")
    void makeNewUpSection() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        Station downStationC = INITIAL_STATION_C.FIND_STATION;
        Station upStationB = STATION_B.createStationToInsert(line2);
        Section downSectionToSave = new Section(null,
                NearbyStations.createByUpStationAndDownStation(upStationB, downStationC),
                line2, SECTION_B_TO_C.DISTANCE
        );
        Section currentSection = INITIAL_SECTION_A_TO_C.FIND_SECTION;

        Section expectedSection = SECTION_A_TO_B.createSectionToInsert(
                INITIAL_STATION_A.FIND_STATION, upStationB, line2);

        // when
        Section newUpSection = NewSectionMaker.makeNewUpSection(downSectionToSave, currentSection);

        // then
        assertThat(newUpSection).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expectedSection);
    }

    @Test
    @DisplayName("등록할 상행 구간과 기존 구간을 받아서 새로운 하행 구간을 생성한다.")
    void makeNewDownSection() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        Station upStationA = INITIAL_STATION_A.FIND_STATION;
        Station downStationB = STATION_B.createStationToInsert(line2);
        Section upSectionToSave = new Section(null,
                NearbyStations.createByUpStationAndDownStation(upStationA, downStationB),
                line2, SECTION_A_TO_B.DISTANCE
        );
        Section currentSection = INITIAL_SECTION_A_TO_C.FIND_SECTION;

        Section expectedSection = SECTION_B_TO_C.createSectionToInsert(
                downStationB, INITIAL_STATION_C.FIND_STATION, line2);

        // when
        Section newDownSection = NewSectionMaker.makeNewDownSection(upSectionToSave, currentSection);

        // then
        assertThat(newDownSection).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(expectedSection);
    }

    @ParameterizedTest
    @ValueSource(ints = {INITIAL_SECTION_A_TO_C.RAW_DISTANCE, INITIAL_SECTION_A_TO_C.RAW_DISTANCE + 1})
    @DisplayName("등록할 구간과 기존 구간을 받아서 새로운 상행 구간을 생성할 때, 등록할 구간의 거리가 기존 구간 거리보다 크거나 같으면 예외가 발생한다.")
    void makeNewSection_throw_sectionDistanceToSave_isGreaterThanEqual_currentSectionDistance(int sectionToSaveDistance) {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        Station downStationC = INITIAL_STATION_C.FIND_STATION;
        Station stationB = STATION_B.createStationToInsert(line2);
        Section downSectionToSave = new Section(null,
                NearbyStations.createByUpStationAndDownStation(stationB, downStationC),
                line2, new Distance(sectionToSaveDistance)
        );

        Station upStationA = INITIAL_STATION_A.FIND_STATION;
        Section upSectionToSave = new Section(null,
                NearbyStations.createByUpStationAndDownStation(upStationA, stationB),
                line2, new Distance(sectionToSaveDistance)
        );
        Section currentSection = INITIAL_SECTION_A_TO_C.FIND_SECTION;

        // when, then
        assertAll(
                () -> assertThatThrownBy(() -> NewSectionMaker.makeNewUpSection(downSectionToSave, currentSection))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("등록할 구간의 길이는 기존 구간 길이보다 작아야합니다."),

                () -> assertThatThrownBy(() -> NewSectionMaker.makeNewDownSection(upSectionToSave, currentSection))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("등록할 구간의 길이는 기존 구간 길이보다 작아야합니다.")
        );
    }
}

