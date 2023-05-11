package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql({"/test-schema.sql", "/test-data.sql"})
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        this.sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Section을 저장한다.")
    void insertTest() {
        // given
        SectionEntity insertEntity = 강변_TO_건대_INSERT_SECTION_ENTITY;

        // when
        Long insertedSectionId = sectionDao.insert(insertEntity);

        // then
        assertThat(insertedSectionId).isEqualTo(2L);
    }

    @Test
    @DisplayName("상행역의 id와 노선 id에 해당하는 행을 조회한다.")
    void findByUpStationIdAndLindIdTest() {
        // given
        Long upStationId = DUMMY_잠실_INSERTED_ID;
        Long lineId = DUMMY_LINE2_ID;

        // when
        Optional<SectionEntity> findSection = sectionDao.findByUpStationIdAndLindId(upStationId, lineId);

        // then
        assertThat(findSection.get()).isEqualTo(잠실_TO_건대_FIND_SECTION_ENTITY);
    }

    @Test
    @DisplayName("상행역의 id와 노선 id에 해당하는 행을 조회한다.")
    void findByDownStationIdAndLindIdTest() {
        // given
        Long downStationId = DUMMY_건대_INSERTED_ID;
        Long lineId = DUMMY_LINE2_ID;

        // when
        Optional<SectionEntity> findSection = sectionDao.findByDownStationIdAndLindId(downStationId, lineId);

        // then
        assertThat(findSection.get()).isEqualTo(잠실_TO_건대_FIND_SECTION_ENTITY);
    }

    @Test
    @DisplayName("노선 id에 해당하는 모든 행을 조회한다.")
    void findSectionsByLineIdTest() {
        // given
        Long lineId = DUMMY_LINE2_ID;

        // when
        List<SectionEntity> findSections = sectionDao.findSectionsByLineId(lineId);

        // then
        assertThat(findSections).isEqualTo(List.of(잠실_TO_건대_FIND_SECTION_ENTITY));
    }

    @Test
    @DisplayName("sectionId에 해당하는 행을 삭제한다.")
    void deleteBySectionIdTest() {
        // given
        Long sectionIdToDelete = DUMMY_SECTION_잠실_TO_건대_ID;

        // when
        sectionDao.deleteBySectionId(sectionIdToDelete);

        // then
        assertThat(sectionDao.findSectionsByLineId(DUMMY_LINE2_ID)).hasSize(0);
    }
}