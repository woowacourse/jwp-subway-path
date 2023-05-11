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
import subway.entity.StationEntity;
import subway.fixture.StationFixture.삼성역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationEntityDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    private StationDao stationDao;

    private final RowMapper<StationEntity> stationRowMapper = (rs, rowNum) ->
            new StationEntity(
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
        StationEntity stationEntity = stationDao.insert(삼성역.ENTITY);
        StationEntity result = jdbcTemplate.queryForObject("SELECT * FROM station WHERE id = ?", stationRowMapper,
                stationEntity.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void 전체_조회_테스트() {
        jdbcTemplate.update("INSERT INTO station(name) VALUES (?)", "삼성역");
        jdbcTemplate.update("INSERT INTO station(name) VALUES (?)", "역삼역");
        List<StationEntity> stationEntities = stationDao.findAll();
        assertAll(
                () -> assertThat(stationEntities.size())
                        .isEqualTo(2),
                () -> assertThat(stationEntities)
                        .extracting("name")
                        .containsExactlyInAnyOrder("삼성역", "역삼역")
        );
    }

    @Test
    void 아이디로_조회_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "삼성역");

        Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        StationEntity stationEntity = stationDao.findById(stationId).get();

        assertThat(stationEntity.getName()).isEqualTo("삼성역");
    }

    @Test
    void 갱신_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "삼성역");

        Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        stationDao.update(new StationEntity(stationId, "잠실역"));

        StationEntity result = jdbcTemplate.queryForObject("SELECT * FROM station WHERE id = ?", stationRowMapper,
                stationId);

        assertThat(result.getName()).isEqualTo("잠실역");
    }

    @Test
    void 삭제_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "삼성역");

        Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        stationDao.deleteById(stationId);

        List<StationEntity> result = jdbcTemplate.query("SELECT * FROM station WHERE id = ?", stationRowMapper,
                stationId);
        assertThat(result).isEmpty();
    }
}
