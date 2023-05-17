package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Station;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        jdbcTemplate.execute("DELETE FROM section");
    }

    @Test
    void 노선의_id로_노선에_있는_역을_조회할_수_있다() {
        // given
        final Long lineId = lineDao.insert("8호선", "분홍색");

        final StationEntity sourceStationEntity = stationDao.insert("잠실역");
        final StationEntity targetStationEntity = stationDao.insert("석촌역");
        final Long sourceStationId = sourceStationEntity.getId();
        final Long targetStationId = targetStationEntity.getId();

        final SectionEntity sectionId = sectionDao.insert(lineId, sourceStationId, targetStationId, 10);

        // when
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);

        // then
        assertAll(
                () -> assertThat(sectionEntities).hasSize(1),
                () -> assertThat(sectionEntities.get(0))
                        .extracting( "lineId", "sourceStationId", "targetStationId", "distance")
                        .containsExactly( lineId, sourceStationId, targetStationId, 10));
    }


    @Test
    void 노선_id를_가진_구간을_삭제할_수_있다() {
        // given
        final Long lineId = lineDao.insert("8호선", "분홍색");

        final StationEntity sourceStationEntity = stationDao.insert("잠실역");
        final StationEntity targetStationEntity = stationDao.insert("석촌역");
        final Long sourceStationId = sourceStationEntity.getId();
        final Long targetStationId = targetStationEntity.getId();

        sectionDao.insert(lineId, sourceStationId, targetStationId, 10);

        // when
        sectionDao.deleteByLineId(lineId);

        // then
        final List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);
        assertThat(sectionEntities).isEmpty();
    }

    @Test
    void 전체_구간을_추가할_수_있다() {
        // given
        final Long lineId = lineDao.insert("8호선", "분홍색");

        final StationEntity firstStation = stationDao.insert("잠실역");
        final StationEntity secondStation = stationDao.insert("석촌역");
        final StationEntity thirdStation = stationDao.insert("송파역");
        final Long firstStationId = firstStation.getId();
        final Long secondStationId = secondStation.getId();
        final Long thirdStationId = thirdStation.getId();

        final SectionEntity firstSectionEntity = new SectionEntity(lineId, firstStationId, secondStationId, 10);
        final SectionEntity secondSectionEntity = new SectionEntity(lineId, secondStationId, thirdStationId, 10);

        // when
        sectionDao.insertAll(List.of(firstSectionEntity, secondSectionEntity));

        // then
        final List<SectionEntity> sectionEntities = sectionDao.findAll();
        assertThat(sectionEntities).hasSize(2);
    }

}
