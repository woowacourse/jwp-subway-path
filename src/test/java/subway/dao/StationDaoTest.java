package subway.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@Sql("classpath:schema.sql")
class StationDaoTest {

    private final StationDao stationDao;

    private StationDaoTest(@Autowired final JdbcTemplate jdbcTemplate) {
        this.stationDao = new StationDao(jdbcTemplate);
    }

    @Test
    @DisplayName("이름으로 Station 을 조회한다")
    void findByName() {
        // given
        final Station station = new Station("잠실역");
        stationDao.insert(station);

        // when
        final Station foundStation = stationDao.findByName(station.getName());

        // then
        assertThat(foundStation.getName()).isEqualTo(station.getName());
    }
}
