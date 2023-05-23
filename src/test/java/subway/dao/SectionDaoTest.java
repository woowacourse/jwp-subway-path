package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;


    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
        stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
        sectionDao = new SectionDao(jdbcTemplate);
    }

    @Test
    @DisplayName("노선에 구간을 추가한다.")
    void insertToLine() {
        LineEntity lineEntity = lineDao.insert(new LineEntity(null, "1호선", 500));
        StationEntity stationEntity1 = stationDao.insert(new StationEntity(null, "서울대입구역"));
        StationEntity stationEntity2 = stationDao.insert(new StationEntity(null, "신림역"));
        SectionEntity sectionEntity = new SectionEntity(null, lineEntity.getId(), stationEntity1.getId(),
                stationEntity2.getId(), 10);

        SectionEntity savedSectionEntity = sectionDao.insert(sectionEntity);

        Assertions.assertAll(
                () -> assertThat(savedSectionEntity.getId()).isNotNull(),
                () -> assertThat(savedSectionEntity.getLineId()).isEqualTo(sectionEntity.getLineId()),
                () -> assertThat(savedSectionEntity.getLeftStationId()).isEqualTo(sectionEntity.getLeftStationId()),
                () -> assertThat(savedSectionEntity.getRightStationId()).isEqualTo(sectionEntity.getRightStationId()),
                () -> assertThat(savedSectionEntity.getDistance()).isEqualTo(sectionEntity.getDistance())
        );
    }

    @Test
    @DisplayName("노선에 해당하는 모든 역을 가져온다.")
    void findByLineId() {
        LineEntity lineEntity = lineDao.insert(new LineEntity(null, "1호선", 500));
        StationEntity stationEntity1 = stationDao.insert(new StationEntity(null, "서울대입구역"));
        StationEntity stationEntity2 = stationDao.insert(new StationEntity(null, "신림역"));
        StationEntity stationEntity3 = stationDao.insert(new StationEntity(null, "강남역"));
        SectionEntity sectionEntity1 = new SectionEntity(null, lineEntity.getId(), stationEntity1.getId(),
                stationEntity2.getId(), 10);
        SectionEntity sectionEntity2 = new SectionEntity(null, lineEntity.getId(), stationEntity2.getId(),
                stationEntity3.getId(), 7);

        SectionEntity savedSectionEntity1 = sectionDao.insert(sectionEntity1);
        SectionEntity savedSectionEntity2 = sectionDao.insert(sectionEntity2);

        assertThat(sectionDao.findByLineId(savedSectionEntity1.getLineId())).contains(savedSectionEntity1,
                savedSectionEntity2);
    }

    @Test
    @DisplayName("노선에서 역을 삭제한다.")
    void deleteByStationId() {
        LineEntity lineEntity = lineDao.insert(new LineEntity(null, "1호선", 500));
        StationEntity stationEntity1 = stationDao.insert(new StationEntity(null, "서울대입구역"));
        StationEntity stationEntity2 = stationDao.insert(new StationEntity(null, "신림역"));
        SectionEntity sectionEntity = new SectionEntity(null, lineEntity.getId(), stationEntity1.getId(),
                stationEntity2.getId(), 10);
        SectionEntity savedSectionEntity = sectionDao.insert(sectionEntity);

        sectionDao.deleteByStationId(savedSectionEntity.getId());

        assertThat(sectionDao.findByLineId(lineEntity.getId()).isEmpty()).isTrue();
    }
}
