package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.Station;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    void 역_정보를_삽입한다() {
        // given
        final Station 후추 = new Station("후추");
        // when
        final Station insertedStation = stationDao.insert(후추);
        // then
        assertSoftly(softly -> {
            softly.assertThat(insertedStation.getName()).isEqualTo("후추");
        });
    }

    @Test
    void 전체_역_정보를_조회한다() {
        // given
        final Station 후추 = new Station("후추");
        final Station 디노 = new Station("디노");
        stationDao.insert(후추);
        stationDao.insert(디노);
        // when
        final List<Station> allStations = stationDao.findAll();
        // then
        assertSoftly(softly -> {
            softly.assertThat(allStations).hasSize(2);
            final Station firstStation = allStations.get(0);
            softly.assertThat(firstStation.getName()).isEqualTo("후추");
            final Station secondStation = allStations.get(1);
            softly.assertThat(secondStation.getName()).isEqualTo("디노");
        });
    }

    @Test
    void id로_역_정보를_조회한다() {
        // given
        final Station 후추 = new Station("후추");
        final Long id = stationDao.insert(후추).getId();
        // when
        final Station actualStation = stationDao.findById(id);
        // then
        assertThat(actualStation.getName()).isEqualTo("후추");
    }

    @Test
    void 역_정보를_수정한다() {
        // given
        final Station 후추 = new Station("후추");
        final Long id = stationDao.insert(후추).getId();
        // when
        stationDao.update(new Station(id, "디노"));
        // then
        final Station station = stationDao.findById(id);
        assertThat(station.getName()).isEqualTo("디노");
    }

    @Test
    void 역_정보를_삭제한다() {
        // given
        final Station 후추 = new Station("후추");
        final Long id = stationDao.insert(후추).getId();
        // when
        stationDao.deleteById(id);
        // then
        final List<Station> allStations = stationDao.findAll();
        assertThat(allStations).hasSize(0);
    }

}
