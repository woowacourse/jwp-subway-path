package subway.dao.v2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Distance;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionDaoV2Test extends DaoTestConfig {

    SectionDaoV2 sectionDao;
    StationDaoV2 stationDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDaoV2(jdbcTemplate);
        stationDao = new StationDaoV2(jdbcTemplate);
    }

    @Test
    void 구간을_저장한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("헤나"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("루카"));

        // when
        final SectionEntity sectionEntity = new SectionEntity(10, true, saveUpStationId, saveDownStationId);
//        final Long saveSectionId = sectionDao.insert(saveUpStationId, saveDownStationId, false, new Distance(10));
        final Long saveSectionId = sectionDao.insert(sectionEntity);

        // then
        assertThat(saveSectionId)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 구간을_조회한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("헤나"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("루카"));

        final Long saveSectionId = sectionDao.insert(saveUpStationId, saveDownStationId, true, new Distance(10));

        // when
        final Optional<SectionEntity> maybeSectionEntity = sectionDao.findBySectionId(saveSectionId);

        // then
        assertAll(
                () -> assertThat(maybeSectionEntity).isPresent(),
                () -> assertThat(maybeSectionEntity.get())
                        .usingRecursiveComparison()
                        .isEqualTo(new SectionEntity(saveSectionId, 10, true, saveUpStationId, saveDownStationId))
        );
    }

    @Test
    void 구간을_삭제한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("헤나"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("루카"));

        final Long saveSectionId = sectionDao.insert(saveUpStationId, saveDownStationId, true, new Distance(10));

        // when
        sectionDao.delete(saveSectionId);

        final Optional<SectionEntity> maybeSectionEntity = sectionDao.findBySectionId(saveSectionId);

        // then
        assertThat(maybeSectionEntity).isEmpty();
    }
}
