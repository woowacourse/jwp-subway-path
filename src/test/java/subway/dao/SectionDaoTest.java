package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
public class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 모든_역_구간_정보를_저장한다() {
        final Long lineId = lineDao.save(new LineEntity(2L, "2호선", "초록색"));
        final Long stationId1 = stationDao.save(new StationEntity("잠실역"));
        final Long stationId2 = stationDao.save(new StationEntity("아현역"));
        final Long stationId3 = stationDao.save(new StationEntity("신촌역"));
        final SectionEntity sectionEntity1 = new SectionEntity(lineId, stationId1, stationId2, 3L);
        final SectionEntity sectionEntity2 = new SectionEntity(lineId, stationId2, stationId3, 2L);

        sectionDao.batchSave(List.of(sectionEntity1, sectionEntity2));

        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);

        assertThat(sectionEntities.size()).isEqualTo(2);
    }

    @Test
    void 모든_역_구간_정보를_조회한다() {
        final Long lineId = lineDao.save(new LineEntity(2L, "2호선", "초록색"));
        final Long stationId1 = stationDao.save(new StationEntity("잠실역"));
        final Long stationId2 = stationDao.save(new StationEntity("아현역"));
        final Long stationId3 = stationDao.save(new StationEntity("신촌역"));
        final SectionEntity sectionEntity1 = new SectionEntity(lineId, stationId1, stationId2, 3L);
        final SectionEntity sectionEntity2 = new SectionEntity(lineId, stationId2, stationId3, 2L);

        sectionDao.batchSave(List.of(sectionEntity1, sectionEntity2));

        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);

        final SoftAssertions softAssertions = new SoftAssertions();
        softAssertions.assertThat(sectionEntities.size()).isEqualTo(2);

        softAssertions.assertThat(sectionEntities.get(0).getLineId()).isEqualTo(lineId);
        softAssertions.assertThat(sectionEntities.get(0).getUpStationId()).isEqualTo(stationId1);
        softAssertions.assertThat(sectionEntities.get(0).getDownStationId()).isEqualTo(stationId2);
        softAssertions.assertThat(sectionEntities.get(0).getDistance()).isEqualTo(3L);

        softAssertions.assertThat(sectionEntities.get(1).getLineId()).isEqualTo(lineId);
        softAssertions.assertThat(sectionEntities.get(1).getUpStationId()).isEqualTo(stationId2);
        softAssertions.assertThat(sectionEntities.get(1).getDownStationId()).isEqualTo(stationId3);
        softAssertions.assertThat(sectionEntities.get(1).getDistance()).isEqualTo(2L);
        softAssertions.assertAll();
    }

    @Test
    void 같은_노선_ID를_가진_모든_역_구간_정보를_삭제한다() {
        final Long lineId = lineDao.save(new LineEntity(2L, "2호선", "초록색"));
        final Long stationId1 = stationDao.save(new StationEntity("잠실역"));
        final Long stationId2 = stationDao.save(new StationEntity("아현역"));
        final Long stationId3 = stationDao.save(new StationEntity("신촌역"));
        final SectionEntity sectionEntity1 = new SectionEntity(lineId, stationId1, stationId2, 3L);
        final SectionEntity sectionEntity2 = new SectionEntity(lineId, stationId2, stationId3, 2L);
        sectionDao.batchSave(List.of(sectionEntity1, sectionEntity2));

        sectionDao.deleteAllByLineId(lineId);

        final List<SectionEntity> sectionEntities = sectionDao.findAllByLineId(lineId);
        assertThat(sectionEntities.isEmpty()).isTrue();
    }
}
