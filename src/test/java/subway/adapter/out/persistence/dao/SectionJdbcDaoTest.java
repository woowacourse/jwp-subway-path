package subway.adapter.out.persistence.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import subway.adapter.out.persistence.entity.LineEntity;
import subway.adapter.out.persistence.entity.SectionEntity;
import subway.adapter.out.persistence.entity.StationEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class SectionJdbcDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SectionJdbcDao sectionJdbcDao;
    private StationJdbcDao stationJdbcDao;
    private LineJdbcDao lineJdbcDao;
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @BeforeEach
    void setUp() {
        lineJdbcDao = new LineJdbcDao(namedParameterJdbcTemplate, jdbcTemplate);
        sectionJdbcDao = new SectionJdbcDao(jdbcTemplate);
        stationJdbcDao = new StationJdbcDao(jdbcTemplate);
    }

    @Test
    @DisplayName("구간을 추가한다.")
    void saveSection() {
        Long lineId = lineJdbcDao.createLine(new LineEntity("1호선", 1000));
        stationJdbcDao.createStation(new StationEntity("비버"));
        stationJdbcDao.createStation(new StationEntity("라빈"));
        stationJdbcDao.createStation(new StationEntity("잠실"));

        List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(lineId, "비버", "라빈", 5L),
                new SectionEntity(lineId, "라빈", "잠실", 5L));

        sectionJdbcDao.saveSection(lineId,sectionEntities);

        assertThat(sectionJdbcDao.findAllByLineId(lineId)).usingRecursiveComparison().ignoringFields("id").isEqualTo(sectionEntities);
    }

    @Test
    @DisplayName("노선의 따른 구간을 조회한다.")
    void findAllByLineId() {

        Long lineId1 = lineJdbcDao.createLine(new LineEntity("3호선", 10));
        stationJdbcDao.createStation(new StationEntity("비버니"));
        stationJdbcDao.createStation(new StationEntity("비버통"));
        stationJdbcDao.createStation(new StationEntity("잠실역"));

        List<SectionEntity> sectionEntities1 = List.of(
                new SectionEntity(lineId1, "비버니", "비버통", 5L),
                new SectionEntity(lineId1, "비버통", "잠실역", 5L));


        sectionJdbcDao.saveSection(lineId1, sectionEntities1);

        assertThat(sectionJdbcDao.findAllByLineId(lineId1)).usingRecursiveComparison().ignoringFields("id").isEqualTo(sectionEntities1);
    }
}