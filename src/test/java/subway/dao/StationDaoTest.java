package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.subway.Station;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void init() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    void 역을_저장한다() {
        Long id = stationDao.insert(new Station("잠실역")).getId();
        assertThat(id).isNotNull();
    }

    /**
     * public void deleteById(Long id) {
     * String sql = "delete from STATION where id = ?";
     * jdbcTemplate.update(sql, id);
     * }
     */

    @Test
    void 전체_조회한다() {
        stationDao.insert(new Station("잠실역1"));
        stationDao.insert(new Station("잠실역2"));
        assertThat(stationDao.findAll()).hasSize(2);
    }

    @Test
    void 단건_조회한다() {
        final Long id = stationDao.insert(new Station("잠실역")).getId();
        assertThat(stationDao.findById(id).getId()).isEqualTo(id);
    }

    @Test
    void 역을_수정한다() {
        final Long id = stationDao.insert(new Station("잠실역")).getId();
        Station update = new Station(id,"수정역");

        stationDao.update(update);

        assertThat(stationDao.findById(id).getName()).isEqualTo("수정역");
    }

    @Test
    void 역을_삭제한다() {
        final Long id = stationDao.insert(new Station("잠실역")).getId();

        stationDao.deleteById(id);

        assertThatThrownBy(() -> stationDao.findById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
