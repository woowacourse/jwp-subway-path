package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import javax.sql.DataSource;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;

@JdbcTest
class StationDaoTest {

    private StationDao stationDao;

    @Autowired
    void setUp(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @DisplayName("역을 저장하면 id와 함께 역을 반환한다.")
    @Test
    void insert() {
        final Station result = stationDao.insert(new Station("잠실역"));
        assertAll(
                () -> assertThat(result.getId()).isPositive(),
                () -> assertThat(result.getName()).isEqualTo("잠실역")
        );
    }

    @DisplayName("모든 역을 조회한다.")
    @Test
    @Sql({"classpath:station.sql"})
    void findAll() {
        final List<Station> result = stationDao.findAll();
        assertThat(result).containsExactly(
                new Station(1L),
                new Station(2L),
                new Station(3L),
                new Station(4L),
                new Station(5L),
                new Station(6L),
                new Station(7L),
                new Station(8L),
                new Station(9L),
                new Station(10L),
                new Station(11L)
        );
    }

    @DisplayName("id로 역을 조회한다.")
    @Test
    void findById() {
        final Station station = stationDao.insert(new Station("잠실역"));
        final Long stationId = station.getId();
        final Station result = stationDao.findById(stationId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(stationId),
                () -> assertThat(result.getName()).isEqualTo("잠실역")
        );
    }

    @DisplayName("역을 업데이트 한다.")
    @Test
    void update() {
        final Station station = stationDao.insert(new Station("잠실역"));
        final Long stationId = station.getId();
        stationDao.update(new Station(stationId, "선릉역"));
        final Station result = stationDao.findById(stationId);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(stationId),
                () -> assertThat(result.getName()).isEqualTo("선릉역")
        );
    }

    @DisplayName("역을 삭제한다.")
    @Test
    void deleteById() {
        final Station station = stationDao.insert(new Station("잠실역"));
        final Long stationId = station.getId();
        assertThatCode(() -> stationDao.deleteById(stationId))
                .doesNotThrowAnyException();
    }
}
