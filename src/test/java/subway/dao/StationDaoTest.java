package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate);
    }

    @DisplayName("한 역의 정보를 등록한다")
    @Test
    void insert() {
        //given
        final Station station = new Station("역");

        //when
        final Station inserted = stationDao.insert(station);

        //then
        assertThat(inserted.getId()).isSameAs(1L);
    }


    @DisplayName("두 역의 정보를 등록한다")
    @Test
    void insertAll() {
        //given
        final List<Station> stations = List.of(new Station("역1"), new Station("역2"));

        //when
        final List<Station> persisted = stationDao.insertAll(stations);

        //then
        assertThat(persisted.get(0).getId()).isSameAs(1L);
        assertThat(persisted.get(1).getId()).isSameAs(2L);
    }

    @DisplayName("역의 ID 정보로 역을 조회한다")
    @Test
    void findById() {
        //given
        final Station station = new Station("역");

        //when
        stationDao.insert(station);
        final Station extracted = stationDao.findById(1L).get();

        //then
        assertThat(extracted.getId()).isEqualTo(1L);
    }

    @DisplayName("역의 ID정보로 역을 삭제한다")
    @Test
    void deleteById() {
        //given
        final Station station = new Station("역");

        //when
        stationDao.insert(station);
        stationDao.deleteById(1L);
        String sql = "SELECT * FROM station";
        final List<Station> stations = jdbcTemplate.query(sql, (rs, rowNum) ->
                new Station(
                        rs.getLong("id"),
                        rs.getString("name")
                ));

        //then
        assertThat(stations).isEmpty();
    }

}