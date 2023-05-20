package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.entity.SectionEntity;

import java.util.Optional;

import static fixtures.LineFixtures.LINE2_ID;
import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.STATION_건대역_ID;
import static fixtures.StationFixtures.STATION_잠실역_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

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
    @DisplayName("상행역 id, 하행역 id, 거리에 해당하는 행을 조회한다.")
    void findByStationIdsAndDistanceTest() {
        // given
        Long upStationId = STATION_잠실역_ID;
        Long downStationId = STATION_건대역_ID;
        int distance = DISTANCE_잠실역_TO_건대역;
        SectionEntity expectSectionEntity = ENTITY_잠실역_TO_건대역_FIND;

        // when
        Optional<SectionEntity> findSection = sectionDao.findByStationIdsAndDistance(upStationId, downStationId, distance);

        // then
        assertThat(findSection).contains(expectSectionEntity);
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
    @DisplayName("sectionId에 해당하는 행을 삭제한다.")
    void deleteBySectionIdTest() {
        // given
        Long sectionIdToDelete = SECTION_잠실역_TO_건대역_ID;

        // when, then
        assertThatNoException().isThrownBy(() -> sectionDao.deleteBySectionId(sectionIdToDelete));
    }
}