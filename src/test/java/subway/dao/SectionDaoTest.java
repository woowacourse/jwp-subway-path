package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.config.DaoTestConfig;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionDaoTest extends DaoTestConfig {

    SectionDao sectionDao;
    StationDao stationDao;
    LineDao lineDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
    }

    @Test
    void 구간을_저장한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("헤나"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("루카"));

        final Long saveLineId = lineDao.insert("2", "초록");

        // when
        final SectionEntity sectionEntity = new SectionEntity(10, true, saveUpStationId, saveDownStationId, saveLineId);

        final Long saveSectionId = sectionDao.insert(sectionEntity);

        // then
        assertThat(saveSectionId)
                .isNotNull()
                .isNotZero();
    }

    @Test
    void 구간을_모두_배치_저장한다() {
        // given
        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(10, false, 1L, 2L, 3L),
                new SectionEntity(10, false, 2L, 3L, 3L),
                new SectionEntity(10, false, 3L, 4L, 3L));

        // when
        sectionDao.insertBatch(sectionEntities);

        final List<SectionEntity> findSectionEntities = sectionDao.findAllByLineId(3L);

        // then
        assertThat(findSectionEntities)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactlyElementsOf(sectionEntities);
    }

    @Test
    void 구간을_조회한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("헤나"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("루카"));

        final Long saveLineId = lineDao.insert("2", "초록");

        final Long saveSectionId = sectionDao.insert(saveUpStationId, saveDownStationId, saveLineId, true, 10);

        // when
        final Optional<SectionEntity> maybeSectionEntity = sectionDao.findBySectionId(saveSectionId);

        // then
        assertAll(
                () -> assertThat(maybeSectionEntity).isPresent(),
                () -> assertThat(maybeSectionEntity.get())
                        .usingRecursiveComparison()
                        .isEqualTo(new SectionEntity(saveSectionId, 10, true, saveUpStationId, saveDownStationId, saveLineId))
        );
    }

    @Test
    void 노선_식별자값에_해당하는_구간들을_모두_조회한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("헤나"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("루카"));
        final Long saveLineId = lineDao.insert("2", "초록");

        sectionDao.insert(saveUpStationId, saveDownStationId, saveLineId, true, 10);

        // when
        final List<SectionEntity> findSectionEntities = sectionDao.findAllByLineId(saveLineId);

        // then
        assertThat(findSectionEntities)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .containsExactly(new SectionEntity(
                        10, true, saveUpStationId, saveDownStationId, saveLineId
                ));
    }

    @Test
    void 구간_식별자값으로_구간을_삭제한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("헤나"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("루카"));

        final Long saveLineId = lineDao.insert("2", "초록");

        final Long saveSectionId = sectionDao.insert(saveUpStationId, saveDownStationId, saveLineId, true, 10);

        // when
        sectionDao.deleteBySectionId(saveSectionId);

        final Optional<SectionEntity> maybeSectionEntity = sectionDao.findBySectionId(saveSectionId);

        // then
        assertThat(maybeSectionEntity).isEmpty();
    }

    @Test
    void 노선_식별자값으로_구간을_삭제한다() {
        // given
        final Long saveUpStationId = stationDao.insert(new StationEntity("헤나"));
        final Long saveDownStationId = stationDao.insert(new StationEntity("루카"));
        final Long saveLineId = lineDao.insert("2", "초록");

        sectionDao.insert(saveUpStationId, saveDownStationId, saveLineId, true, 10);

        // when
        sectionDao.deleteByLineId(saveLineId);

        final List<SectionEntity> findSectionEntities = sectionDao.findAllByLineId(saveLineId);

        // then
        assertThat(findSectionEntities).isEmpty();
    }
}
