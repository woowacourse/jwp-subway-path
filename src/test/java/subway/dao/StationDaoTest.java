package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.domain.Station;
import subway.fixture.StationFixture.삼성역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    private StationDao stationDao;

    private RowMapper<Station> stationRowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    @BeforeEach
    void setUp() {
        stationDao = new StationDao(jdbcTemplate, jdbcTemplate.getDataSource());
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 삽입_테스트() {
        Station station = stationDao.insert(삼성역.STATION);
        Station result = jdbcTemplate.queryForObject("SELECT * FROM station WHERE id = ?", stationRowMapper,
                station.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void 전체_조회_테스트() {
        jdbcTemplate.update("INSERT INTO station(name) VALUES (?)", "삼성역");
        jdbcTemplate.update("INSERT INTO station(name) VALUES (?)", "역삼역");
        List<Station> stations = stationDao.findAll();
        assertAll(
                () -> assertThat(stations.size())
                        .isEqualTo(2),
                () -> assertThat(stations)
                        .extracting("name")
                        .containsExactlyInAnyOrder("삼성역", "역삼역")
        );
    }

    @Test
    void 아이디로_조회_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "삼성역");

        Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        Station station = stationDao.findById(stationId);

        assertThat(station.getName()).isEqualTo("삼성역");
    }

    @Test
    void 갱신_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "삼성역");

        Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        stationDao.update(new Station(stationId, "잠실역"));

        Station result = jdbcTemplate.queryForObject("SELECT * FROM station WHERE id = ?", stationRowMapper, stationId);

        assertThat(result.getName()).isEqualTo("잠실역");
    }

    @Test
    void 삭제_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "삼성역");

        Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        stationDao.deleteById(stationId);

        List<Station> result = jdbcTemplate.query("SELECT * FROM station WHERE id = ?", stationRowMapper, stationId);
        assertThat(result).isEmpty();
    }
}
