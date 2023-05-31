package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.section.Section;
import subway.domain.section.SectionRepository;
import subway.domain.station.StationRepository;
import subway.exception.DuplicateSectionException;

import java.util.List;

import static fixtures.LineFixtures.LINE2_ID;
import static fixtures.SectionFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class DBSectionRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionRepository sectionRepository;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new StationDao(jdbcTemplate);
        SectionDao sectionDao = new SectionDao(jdbcTemplate);
        StationRepository stationRepository = new DBStationRepository(jdbcTemplate, stationDao);
        sectionRepository = new DBSectionRepository(jdbcTemplate, stationRepository, stationDao, sectionDao);
    }

    @Nested
    @DisplayName("섹션을 저장한다.")
    class addStationsAndSectionTest {

        @Test
        @DisplayName("섹션을 저장할 때 이미 존재하는 구간이라면 예외를 반환한다.")
        void validateExistSectionTest() {
            // given
            Section section = SECTION_잠실역_TO_건대역;

            // when, then
            assertThatThrownBy(() -> sectionRepository.insert(section))
                    .isInstanceOf(DuplicateSectionException.class)
                    .hasMessage("이미 포함되어 있는 구간입니다.");
        }

        @Test
        @DisplayName("섹션을 저장할 때 상행역이 이미 존재한다면 하행역을 추가한 후 섹션을 저장한다.")
        void insertDownStationAndSectionTest() {
            // given
            Section section = SECTION_TO_INSERT_잠실역_TO_강변역;
            Section expectSection = SECTION_잠실역_TO_강변역;

            // when
            Section insertedSection = sectionRepository.insert(section);

            // then
            assertThat(insertedSection).isEqualTo(expectSection);
        }

        @Test
        @DisplayName("섹션을 저장할 때 하행역이 이미 존재한다면 상행역을 추가한 후 섹션을 저장한다.")
        void insertUpStationAndSectionTest() {
            // given
            Section section = SECTION_TO_INSERT_강변역_TO_건대역;
            Section expectSection = SECTION_강변역_TO_건대역;

            // when
            Section insertedSection = sectionRepository.insert(section);

            // then
            assertThat(insertedSection).isEqualTo(expectSection);

        }

        @Test
        @DisplayName("섹션을 저장할 때 상행역과 하행역이 모두 아직 저장되어있지 않다면 두 역을 먼저 저장한 후 섹션을 저장한다.")
        void insertAllStationsAndSectionTest() {
            // given
            Section section = SECTION_TO_INSERT_온수역_TO_철산역;
            Section expectSection = SECTION_온수역_TO_철산역;

            // when
            Section insertedSection = sectionRepository.insert(section);

            // then
            assertThat(insertedSection).isEqualTo(expectSection);
        }
    }

    @Test
    @DisplayName("주어진 노선 id에 해당하는 섹션을 가져온다.")
    void findSectionsByLineIdTest() {
        // given
        Long lineId = LINE2_ID;

        // when
        List<Section> findSection = sectionRepository.findSectionsByLineId(lineId);

        // then
        assertThat(findSection).isEqualTo(List.of(SECTION_잠실역_TO_건대역));
    }

    @Test
    @DisplayName("주어진 노선 id에 해당하는 섹션을 가져온다.")
    void findAllSectionsTest() {
        // given
        List<Section> expect = List.of(SECTION_잠실역_TO_건대역);

        // when
        List<Section> findSection = sectionRepository.findAllSections();

        // then
        assertThat(findSection).isEqualTo(expect);
    }

    @Test
    @DisplayName("섹션을 삭제한다.")
    void removeTest() {
        // given
        Section section = SECTION_잠실역_TO_건대역;
        Long lineId = section.getLineId();

        // when
        sectionRepository.remove(section);

        // then
        assertThat(sectionRepository.findSectionsByLineId(lineId)).isEmpty();
    }
}