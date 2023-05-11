package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.dao.StationDao;
import subway.service.station.domain.Station;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @Test
    void 지하철_역을_추가한다() {
        Station jamsil = new Station("잠실");
        Station savedStation = stationDao.insert(jamsil);

        assertAll(
                () -> assertThat(savedStation.getId()).isPositive(),
                () -> assertThat(savedStation.getName()).isEqualTo(jamsil.getName())
        );

    }

    @Test
    void id로_지하철을_조회한다() {
        Station jamsil = new Station("잠실");
        Station yuksam = new Station("역삼");

        Station jamsilSaved = stationDao.insert(jamsil);
        Station yuksamSaved = stationDao.insert(yuksam);

        Station foundJamsil = stationDao.findById(jamsilSaved.getId());
        Station foundYuksam = stationDao.findById(yuksamSaved.getId());

        assertAll(
                () -> assertThat(foundJamsil.getId()).isPositive(),
                () -> assertThat(foundJamsil.getName()).isEqualTo(jamsilSaved.getName()),
                () -> assertThat(foundYuksam.getId()).isPositive(),
                () -> assertThat(foundYuksam.getName()).isEqualTo(yuksamSaved.getName())
        );

    }

}
