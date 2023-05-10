package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.data.StationFixture.JAMSIL;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.data.StationFixture;
import subway.domain.station.Station;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;

    @BeforeEach
    void setting() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("역을 생성한다.")
    void create_station() {
        // when
        Station result = stationDao.insert(JAMSIL);

        // then
        assertThat(result.getName()).isEqualTo(JAMSIL.getName());
    }

}