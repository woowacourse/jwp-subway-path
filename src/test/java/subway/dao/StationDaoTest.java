package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import subway.domain.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Sql("classpath:test_schema.sql")
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
    void 역_정보를_삽입한다() {
        // given
        final Station huchu = new Station("huchu");
        // when
        final Station insertedStation = stationDao.insert(huchu);
        // then
        assertSoftly(softly -> {
            softly.assertThat(insertedStation.getName()).isEqualTo("huchu");
        });
    }

    @Test
    void 전체_역_정보를_조회한다() {
        // given
        final Station huchu = new Station("huchu");
        final Station dino = new Station("dino");
        stationDao.insert(huchu);
        stationDao.insert(dino);
        // when
        final List<Station> allStations = stationDao.findAll();
        // then
        assertSoftly(softly -> {
            softly.assertThat(allStations).hasSize(2);
            final Station firstStation = allStations.get(0);
            softly.assertThat(firstStation.getName()).isEqualTo("huchu");
            final Station secondStation = allStations.get(1);
            softly.assertThat(secondStation.getName()).isEqualTo("dino");
        });
    }

    @Test
    void id로_역_정보를_조회한다() {
        // given
        final Station huchu = new Station("huchu");
        final Long id = stationDao.insert(huchu).getId();
        // when
        final Optional<Station> actualStation = stationDao.findById(id);
        // then
        assertThat(actualStation.get().getName()).isEqualTo("huchu");
    }

    @Test
    void 역_정보를_수정한다() {
        // given
        final Station huchu = new Station("huchu");
        final Long id = stationDao.insert(huchu).getId();
        // when
        stationDao.update(new Station(id, "dino"));
        // then
        final Optional<Station> station = stationDao.findById(id);
        assertThat(station.get().getName()).isEqualTo("dino");
    }

    @Test
    void 역_정보를_삭제한다() {
        // given
        final Station huchu = new Station("huchu");
        final Long id = stationDao.insert(huchu).getId();
        final List<Station> beforeStations = stationDao.findAll();

        // when
        stationDao.deleteById(id);
        // then
        final List<Station> afterStations = stationDao.findAll();
        assertThat(beforeStations).hasSize(afterStations.size() + 1);
    }

}
