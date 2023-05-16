package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class SectionDaoTest {

    private SectionDao sectionDao;
    private LineDao lineDao;
    private StationDao stationDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        lineDao = new LineDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("Section을 생성한다.")
    void insertAll() {
        Long lineId = lineDao.insert(new LineEntity("1호선"));
        StationEntity stationEntity1 = new StationEntity("잠실역");
        Long stationId1 = stationDao.insert(stationEntity1);
        StationEntity stationEntity2 = new StationEntity("잠실새내역");
        Long stationId2 = stationDao.insert(stationEntity2);
        SectionEntity section = new SectionEntity(lineId, stationId1, stationId2, 5);

        List<SectionEntity> sectionEntities = List.of(section);
        sectionDao.insertAll(sectionEntities);

        assertThat(sectionDao.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("lineId로 Section을 조회한다.")
    void findByLineId() {
        Long lineId = lineDao.insert(new LineEntity("1호선"));
        StationEntity stationEntity1 = new StationEntity("잠실역");
        Long stationId1 = stationDao.insert(stationEntity1);
        StationEntity stationEntity2 = new StationEntity("잠실새내역");
        Long stationId2 = stationDao.insert(stationEntity2);
        SectionEntity section = new SectionEntity(lineId, stationId1, stationId2, 5);
        List<SectionEntity> sectionEntities = List.of(section);
        sectionDao.insertAll(sectionEntities);

        List<SectionEntity> sections = sectionDao.findByLineId(lineId);
        assertAll(
                () -> assertThat(sections.get(0).getStartStationId()).isEqualTo(stationId1),
                () -> assertThat(sections.get(0).getEndStationId()).isEqualTo(stationId2)
        );
    }

    @Test
    @DisplayName("lineId로 Section을 삭제한다.")
    void deleteAllById() {
        Long lineId = lineDao.insert(new LineEntity("1호선"));
        StationEntity stationEntity1 = new StationEntity("잠실역");
        Long stationId1 = stationDao.insert(stationEntity1);
        StationEntity stationEntity2 = new StationEntity("잠실새내역");
        Long stationId2 = stationDao.insert(stationEntity2);
        SectionEntity section = new SectionEntity(lineId, stationId1, stationId2, 5);
        List<SectionEntity> sectionEntities = List.of(section);
        sectionDao.insertAll(sectionEntities);

        sectionDao.deleteAllById(lineId);

        assertThat(sectionDao.findByLineId(lineId).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("모든 section을 조회한다.")
    void findAll() {
        Long lineId = lineDao.insert(new LineEntity("1호선"));
        StationEntity stationEntity1 = new StationEntity("잠실역");
        Long stationId1 = stationDao.insert(stationEntity1);
        StationEntity stationEntity2 = new StationEntity("잠실새내역");
        Long stationId2 = stationDao.insert(stationEntity2);
        SectionEntity section = new SectionEntity(lineId, stationId1, stationId2, 5);
        List<SectionEntity> sectionEntities = List.of(section);
        sectionDao.insertAll(sectionEntities);

        List<SectionEntity> sections = sectionDao.findAll();

        assertAll(
                () -> assertThat(sections.get(0).getStartStationId()).isEqualTo(stationId1),
                () -> assertThat(sections.get(0).getEndStationId()).isEqualTo(stationId2)
        );
    }

    @Test
    @DisplayName("stationId로 section이 존재하는지 확인한다.")
    void findExistStationById() {
        Long lineId = lineDao.insert(new LineEntity("1호선"));
        StationEntity stationEntity1 = new StationEntity("잠실역");
        Long stationId1 = stationDao.insert(stationEntity1);
        StationEntity stationEntity2 = new StationEntity("잠실새내역");
        Long stationId2 = stationDao.insert(stationEntity2);
        SectionEntity section = new SectionEntity(lineId, stationId1, stationId2, 5);
        List<SectionEntity> sectionEntities = List.of(section);
        sectionDao.insertAll(sectionEntities);

        assertAll(
                () -> assertThat(sectionDao.findExistStationById(stationId1)).isTrue(),
                () -> assertThat(sectionDao.findExistStationById(stationId2)).isTrue(),
                () -> assertThat(sectionDao.findExistStationById(3L)).isFalse()
        );
    }
}
