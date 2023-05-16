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
        SectionEntity entityToInsert = ENTITY_잠실역_TO_강변역_INSERT;

        // when
        SectionEntity insertedSectionEntity = sectionDao.insert(entityToInsert);

        // then
        assertThat(insertedSectionEntity).isEqualTo(ENTITY_잠실역_TO_강변역_FIND);
    }

    @Test
    @DisplayName("상행역의 id와 노선 id에 해당하는 행을 조회한다.")
    void findByUpStationIdAndLindIdTest() {
        // given
        Long upStationId = STATION_잠실역_ID;
        Long lineId = LINE2_ID;

        // when
        Optional<SectionEntity> findSectionEntity = sectionDao.findByUpStationIdAndLindId(upStationId, lineId);

        // then
        assertThat(findSectionEntity).contains(ENTITY_잠실역_TO_건대역_FIND);
    }

    @Test
    @DisplayName("상행역의 id와 노선 id에 해당하는 행을 조회한다.")
    void findByDownStationIdAndLindIdTest() {
        // given
        Long downStationId = STATION_건대역_ID;
        Long lineId = LINE2_ID;

        // when
        Optional<SectionEntity> findSectionEntity = sectionDao.findByDownStationIdAndLindId(downStationId, lineId);

        // then
        assertThat(findSectionEntity).contains(ENTITY_잠실역_TO_건대역_FIND);
    }

    @Test
    @DisplayName("노선 id에 해당하는 모든 행을 조회한다.")
    void findSectionsByLineIdTest() {
        // given
        Long lineId = LINE2_ID;

        // when
        List<SectionEntity> findSectionEntities = sectionDao.findSectionEntitiesByLineId(lineId);

        // then
        assertThat(findSectionEntities).isEqualTo(List.of(ENTITY_잠실역_TO_건대역_FIND));
    }

    @Test
    @DisplayName("sectionId에 해당하는 행을 삭제한다.")
    void deleteBySectionIdTest() {
        // given
        Long sectionIdToDelete = SECTION_잠실역_TO_건대역_ID;

        // when
        sectionDao.deleteBySectionId(sectionIdToDelete);

        // then
        assertThat(sectionDao.findSectionEntitiesByLineId(LINE2_ID)).isEmpty();
    }
}