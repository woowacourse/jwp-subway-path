package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.persistence.dao.StationDao;
import subway.persistence.dao.entity.StationEntity;
import subway.service.station.domain.Station;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.StationEntityFixture.JAMSIL_NO_ID_ENTITY;
import static subway.fixture.StationEntityFixture.YUKSAM_NO_ID_ENTITY;

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
        StationEntity savedEntity = stationDao.insert(JAMSIL_NO_ID_ENTITY);

        assertAll(
                () -> assertThat(savedEntity.getId()).isPositive()
        );

    }

    @Test
    void id로_지하철을_조회한다() {
        Station jamsil = new Station("잠실");
        Station yuksam = new Station("역삼");

        StationEntity savedJamsil = stationDao.insert(JAMSIL_NO_ID_ENTITY);
        StationEntity savedYuksam = stationDao.insert(YUKSAM_NO_ID_ENTITY);

        assertAll(
                () -> assertThat(savedJamsil.getId()).isPositive(),
                () -> assertThat(savedJamsil.getName()).isEqualTo(jamsil.getName()),
                () -> assertThat(savedYuksam.getId()).isPositive(),
                () -> assertThat(savedYuksam.getName()).isEqualTo(yuksam.getName())
        );

    }

}
