package subway.dao;

import static fixtures.GeneralSectionFixtures.*;
import static fixtures.LineFixtures.INITIAL_Line2;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.line.Line;
import subway.domain.section.general.GeneralSection;
import subway.domain.station.Station;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class GeneralSectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GeneralSectionDao generalSectionDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        this.generalSectionDao = new GeneralSectionDao(jdbcTemplate);
        this.stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Section을 저장한다.")
    void insertTest() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        Station insertedStationB = stationDao.insert(STATION_B.createStationToInsert(line2));
        Station stationC = INITIAL_STATION_C.FIND_STATION;

        GeneralSection sectionToInsert = GENERAL_SECTION_B_TO_C.createSectionToInsert(insertedStationB, stationC, line2);

        // when
        GeneralSection insertedSectionBtoC = generalSectionDao.insert(sectionToInsert);

        // then
        assertThat(insertedSectionBtoC).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(sectionToInsert);
    }

    @Test
    @DisplayName("상행역의 이름와 노선 이름에 해당하는 행을 조회한다.")
    void findByUpStationNameAndLindNameTest() {
        // given
        String upStationName = INITIAL_STATION_A.NAME;
        String lineName = INITIAL_Line2.NAME;

        // when
        Optional<GeneralSection> findSection = generalSectionDao.selectByUpStationNameAndLineName(upStationName, lineName);

        // then
        assertThat(findSection.get()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(INITIAL_GENERAL_SECTION_A_TO_C.FIND_SECTION);
    }

    @ParameterizedTest
    @CsvSource(value = {"B역:2호선", "A역:1호선"}, delimiter = ':')
    @DisplayName("상행역의 이름와 노선 이름에 해당하는 행이 없으면 빈 Optional을 반환한다.")
    void findByUpStationNameAndLindNameEmptyOptional(String upStationName, String lineName) {
        // when
        Optional<GeneralSection> findSection = generalSectionDao.selectByUpStationNameAndLineName(upStationName, lineName);

        // then
        assertThat(findSection.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("하행역의 이름와 노선 이름에 해당하는 행을 조회한다.")
    void findByDownStationNameAndLindNameTest() {
        // given
        String downStationName = INITIAL_STATION_C.NAME;
        String lineName = INITIAL_Line2.NAME;

        // when
        Optional<GeneralSection> findSection = generalSectionDao.selectByDownStationNameAndLineName(downStationName, lineName);

        // then
        assertThat(findSection.get()).usingRecursiveComparison()
                .ignoringFields("id").isEqualTo(INITIAL_GENERAL_SECTION_A_TO_C.FIND_SECTION);
    }

    @ParameterizedTest
    @CsvSource(value = {"A역:2호선", "B역:1호선"}, delimiter = ':')
    @DisplayName("하행역의 이름와 노선 이름에 해당하는 행이 없으면 빈 Optional을 반환한다.")
    void findByDownStationNameAndLindNameEmptyOptional(String downStationName, String lineName) {
        // when
        Optional<GeneralSection> findSection = generalSectionDao.selectByDownStationNameAndLineName(downStationName, lineName);

        // then
        assertThat(findSection.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @CsvSource(value = {"1:False", "2:True"}, delimiter = ':')
    @DisplayName("구간 ID에 해당하는 행이 있으면 False, 없으면 True를 반환한다.")
    void isNotExistById(String id, Boolean expected) {
        // given
        Long parsedId = Long.parseLong(id);

        // when, then
        assertThat(generalSectionDao.isNotExistById(parsedId)).isEqualTo(expected);
    }

    @Test
    @DisplayName("노선 id에 해당하는 모든 행을 조회한다.")
    void findSectionsByLineIdTest() {
        // given
        Line line2 = INITIAL_Line2.FIND_LINE;
        Station stationToInsert = STATION_B.createStationToInsert(line2);

        Station insertedStationB = stationDao.insert(stationToInsert);
        GeneralSection insertedSectionAtoB =
                generalSectionDao.insert(GENERAL_SECTION_A_TO_B.createSectionToInsert(INITIAL_STATION_A.FIND_STATION, insertedStationB, line2));

        // when
        List<GeneralSection> findSections = generalSectionDao.selectAllSectionByLineId(line2.getId());

        // then
        assertAll(
                () -> assertThat(findSections.size()).isEqualTo(2),
                () -> assertThat(findSections).usingRecursiveFieldByFieldElementComparator()
                        .contains(INITIAL_GENERAL_SECTION_A_TO_C.FIND_SECTION, insertedSectionAtoB)
        );
    }

    @Test
    @DisplayName("구간 ID에 해당하는 행을 삭제한다.")
    void deleteBySectionIdTest() {
        // given
        Long sectionIdToDelete = INITIAL_GENERAL_SECTION_A_TO_C.ID;

        // when
        generalSectionDao.deleteById(sectionIdToDelete);

        // then
        assertThat(generalSectionDao.selectAllSectionByLineId(INITIAL_Line2.ID)).isEmpty();
    }
}
