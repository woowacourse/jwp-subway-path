package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;
import java.util.List;

@JdbcTest
@Sql("/schema.sql")
class StationDaoTest {

    private StationDao stationDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        this.stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    void 역_삽입() {
        // given
        Station station = new Station(1L, "신림");

        // when
        Station savedStation = stationDao.insert(station);

        // then
        assertThat(station).isEqualTo(savedStation);
    }

    @Test
    void ID가_없는_역_삽입() {
        // given
        Station station = new Station("신림");

        // when
        Station savedStation = stationDao.insert(station);

        // then
        assertAll(
                () -> assertThat(savedStation.getId()).isEqualTo(1L),
                () -> assertThat(savedStation.getName()).isEqualTo("신림")
        );
    }

    @Test
    void ID로_단일_역_조회() {
        // given
        long id = 1L;
        Station station = new Station(id, "신림");
        stationDao.insert(station);

        // when
        Station foundStation = stationDao.findById(id);

        // then
        assertThat(foundStation).isEqualTo(station);
    }

    @Test
    void 모든_역_조회() {
        // given
        Station 신림 = new Station(1L, "신림");
        Station 봉천 = new Station(2L, "봉천");
        Station 낙성대 = new Station(3L, "낙성대");

        stationDao.insert(신림);
        stationDao.insert(봉천);
        stationDao.insert(낙성대);

        // when
        List<Station> stations = stationDao.findAll();

        // then
        assertAll(
                () -> assertThat(stations).hasSize(3),
                () -> assertThat(stations).contains(신림, 봉천, 낙성대)
        );
    }

    @Test
    void 역_수정() {
        // given
        long id = 1L;
        Station station = new Station(id, "신림");
        stationDao.insert(station);

        // when
        stationDao.update(new Station(id, "봉천"));

        // then
        assertThat(stationDao.findById(id)).isEqualTo(new Station(id, "봉천"));
    }

    @Test
    void ID로_역_삭제() {
        // given
        long id = 1L;
        Station station = new Station(id, "신림");
        stationDao.insert(station);

        // when
        stationDao.deleteById(id);

        // then
        assertThat(stationDao.findAll()).hasSize(0);
    }
}
