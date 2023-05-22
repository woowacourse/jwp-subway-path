package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

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
        Long lineId = lineDao.insert(new Line("1호선"));
        Station station1 = new Station("잠실역");
        Station station2 = new Station("잠실새내역");
        Long stationId1 = stationDao.insert(station1);
        Long stationId2 = stationDao.insert(station2);
        Section section = new Section(lineId, new Station(stationId1, "잠실역"), new Station(stationId2, "잠실새내역"), new Distance(5));

        List<Section> sections = List.of(section);
        sectionDao.insertAll(lineId, sections);

        assertThat(sectionDao.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("lineId로 Section을 조회한다.")
    void findByLineId() {
        Long lineId = lineDao.insert(new Line(1L, "1호선"));
        Station station1 = new Station("잠실역");
        Station station2 = new Station("잠실새내역");
        Long stationId1 = stationDao.insert(station1);
        Long stationId2 = stationDao.insert(station2);
        Section section = new Section(lineId, new Station(stationId1, "잠실역"), new Station(stationId2, "잠실새내역"), new Distance(5));

        sectionDao.insertAll(lineId, List.of(section));

        List<Section> sections = sectionDao.findByLineId(lineId);
        assertAll(
                () -> assertThat(sections.get(0).getStartStation().getName()).isEqualTo(station1.getName()),
                () -> assertThat(sections.get(0).getEndStation().getName()).isEqualTo(station2.getName())
        );
    }

    @Test
    @DisplayName("lineId로 Section을 삭제한다.")
    void deleteAllById() {
        Long lineId = lineDao.insert(new Line(1L, "1호선"));
        Station station1 = new Station(1L, "잠실역");
        Station station2 = new Station(2L, "잠실새내역");
        stationDao.insert(station1);
        stationDao.insert(station2);
        Section section = new Section(lineId, station1, station2, new Distance(5));
        sectionDao.insertAll(1L, List.of(section));

        sectionDao.deleteAllById(lineId);

        assertThat(sectionDao.findByLineId(lineId).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("모든 section을 조회한다.")
    void findAll() {
        Long lineId = lineDao.insert(new Line("1호선"));
        Station station1 = new Station("잠실역");
        Station station2 = new Station("잠실새내역");
        Long stationId1 = stationDao.insert(station1);
        Long stationId2 = stationDao.insert(station2);
        Section section = new Section(lineId, new Station(stationId1, "잠실역"), new Station(stationId2, "잠실새내역"), new Distance(5));

        sectionDao.insertAll(lineId, List.of(section));

        List<Section> sections = sectionDao.findAll();

        assertAll(
                () -> assertThat(sections.get(0).getStartStation().getName()).isEqualTo(station1.getName()),
                () -> assertThat(sections.get(0).getEndStation().getName()).isEqualTo(station2.getName())
        );
    }

    @Test
    @DisplayName("stationId로 section이 존재하는지 확인한다.")
    void findExistStationById() {
        Long lineId = lineDao.insert(new Line(1L, "1호선"));
        Station station1 = new Station("잠실역");
        Station station2 = new Station("잠실새내역");
        Long stationId1 = stationDao.insert(station1);
        Long stationId2 = stationDao.insert(station2);
        Section section = new Section(lineId, new Station(stationId1, "잠실역"), new Station(stationId2, "잠실새내역"), new Distance(5));

        sectionDao.insertAll(lineId, List.of(section));

        assertAll(
                () -> assertThat(sectionDao.findExistStationById(stationId1)).isTrue(),
                () -> assertThat(sectionDao.findExistStationById(stationId2)).isTrue(),
                () -> assertThat(sectionDao.findExistStationById(3L)).isFalse()
        );
    }
}
