package subway.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.data.LineFixture.LINE2_ENTITY;

@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private StationDao stationDao;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);
    }

    @AfterEach
    void clear() {
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("역 정보를 저장한다.")
    void station_data_insert() {
        // givne
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);
        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());

        // when
        StationEntity result = stationDao.insert(jamsil);

        // then
        assertThat(result.getName()).isEqualTo(jamsil.getName());
        assertThat(result.getLineId()).isEqualTo(jamsil.getLineId());
    }

    @Test
    @DisplayName("역 정보를 불러온다.")
    void station_data_load() {
        // givne
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);
        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);

        // when
        Optional<StationEntity> result = stationDao.findById(insertedJamsil.getId());

        // then
        assertThat(result.get().getName()).isEqualTo(insertedJamsil.getName());
        assertThat(result.get().getLineId()).isEqualTo(insertedJamsil.getLineId());
    }
}