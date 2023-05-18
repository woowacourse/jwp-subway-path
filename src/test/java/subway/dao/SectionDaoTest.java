package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@JdbcTest
class SectionDaoTest {

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

    @DisplayName("구간을 저장한다.")
    @Test
    void save() {
        // given
        Long lineId = 1L;
        Long leftStationId = 1L;
        Long rightStationId = 2L;

        LineEntity lineEntity = new LineEntity(lineId, "1호선");
        StationEntity leftStationEntity = new StationEntity(leftStationId, "강남역");
        StationEntity rightStationEntity = new StationEntity(rightStationId, "서초역");
        LineEntity line = lineDao.save(lineEntity);
        StationEntity station1 = stationDao.save(leftStationEntity);
        StationEntity station2 = stationDao.save(rightStationEntity);

        SectionEntity sectionEntity = new SectionEntity(1L, line.getId(), station1.getId(), station2.getId(), 5);

        // when
        SectionEntity newSectionEntity = sectionDao.save(sectionEntity);

        // then
        assertSoftly(softly -> {
            softly.assertThat(newSectionEntity.getLineId()).isEqualTo(line.getId());
            softly.assertThat(newSectionEntity.getLeftStationId()).isEqualTo(station1.getId());
            softly.assertThat(newSectionEntity.getRightStationId()).isEqualTo(station2.getId());
        });
    }

    @DisplayName("모든 구간을 저장 및 조회한다.")
    @Test
    void saveAll() {
        // given
        Long lineId = 1L;
        Long stationId1 = 1L;
        Long stationId2 = 2L;
        Long stationId3 = 3L;

        LineEntity lineEntity = new LineEntity(lineId, "1호선");
        StationEntity stationEntity1 = new StationEntity(stationId1, "강남역");
        StationEntity stationEntity2 = new StationEntity(stationId2, "서초역");
        StationEntity stationEntity3 = new StationEntity(stationId3, "잠실역");
        LineEntity line = lineDao.save(lineEntity);
        StationEntity station1 = stationDao.save(stationEntity1);
        StationEntity station2 = stationDao.save(stationEntity2);
        StationEntity station3 = stationDao.save(stationEntity3);

        SectionEntity sectionEntity1 = new SectionEntity(1L, line.getId(), station1.getId(), station2.getId(), 5);
        SectionEntity sectionEntity2 = new SectionEntity(2L, line.getId(), station2.getId(), station3.getId(), 5);

        List<SectionEntity> sectionEntities = List.of(sectionEntity1, sectionEntity2);

        // when
        sectionDao.saveAll(sectionEntities);

        // then
        assertThat(sectionDao.findAllByLineId(line.getId())).hasSize(2);
    }

    @DisplayName("id에 해당되는 호선에 존재하는 모든 구간을 삭제한다.")
    @Test
    void deleteAllByLineId() {
        // given
        Long lineId = 1L;
        Long stationId1 = 1L;
        Long stationId2 = 2L;
        Long stationId3 = 3L;

        LineEntity lineEntity = new LineEntity(lineId, "1호선");
        StationEntity stationEntity1 = new StationEntity(stationId1, "강남역");
        StationEntity stationEntity2 = new StationEntity(stationId2, "서초역");
        StationEntity stationEntity3 = new StationEntity(stationId3, "잠실역");
        LineEntity line = lineDao.save(lineEntity);
        StationEntity station1 = stationDao.save(stationEntity1);
        StationEntity station2 = stationDao.save(stationEntity2);
        StationEntity station3 = stationDao.save(stationEntity3);

        SectionEntity sectionEntity1 = new SectionEntity(1L, line.getId(), station1.getId(), station2.getId(), 5);
        SectionEntity sectionEntity2 = new SectionEntity(2L, line.getId(), station2.getId(), station3.getId(), 5);

        sectionDao.saveAll(List.of(sectionEntity1, sectionEntity2));

        // when
        sectionDao.deleteAllByLineId(lineId);

        // then
        assertThat(sectionDao.findAllByLineId(lineId)).hasSize(0);
    }
}
