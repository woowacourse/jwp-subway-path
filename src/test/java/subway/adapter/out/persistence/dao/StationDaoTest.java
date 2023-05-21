package subway.adapter.out.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.adapter.out.persistence.entity.StationEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class StationDaoTest {

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
        // given
        StationEntity entity = new StationEntity("역삼역");

        // given
        StationEntity response = stationDao.insert(entity);

        // then
        String sql = "SELECT * FROM station WHERE id = ?";
        StationEntity result = jdbcTemplate.queryForObject(sql, stationRowMapper, response.getId());
        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(entity);
    }

    @Test
    void 전체_조회_테스트() {
        // given
        jdbcTemplate.update("INSERT INTO station(name) VALUES (?)", "삼성역");
        jdbcTemplate.update("INSERT INTO station(name) VALUES (?)", "역삼역");

        // when
        List<StationEntity> stationEntities = stationDao.findAll();

        // then
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
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "삼성역");
        Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        // when
        Optional<StationEntity> station = stationDao.findById(stationId);

        // then
        assertAll(
                () -> assertThat(station).isPresent(),
                () -> assertThat(station.get().getName())
                        .isEqualTo("삼성역")
        );
    }

    @Test
    void 아이디로_조회시_존재하지_않으면_Optional_empty_반환() {
        // when
        Optional<StationEntity> station = stationDao.findById(-1L);

        // then
        assertThat(station).isEmpty();
    }

    @Nested
    class 이름으로_조회시_ {

        @Test
        void 존재하면_Optional_entity_반환() {
            // given
            Map<String, Object> params = new HashMap<>();
            params.put("name", "삼성역");
            Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

            // when
            Optional<StationEntity> station = stationDao.findByName("삼성역");

            // then
            assertAll(
                    () -> assertThat(station).isPresent(),
                    () -> assertThat(station.get().getName())
                            .isEqualTo("삼성역")
            );
        }

        @Test
        void 존재하지_않으면_Optional_empty_반환() {
            // when
            Optional<StationEntity> station = stationDao.findByName("삼성역");

            // then
            assertThat(station).isEmpty();
        }
    }

    @Test
    void 갱신_테스트() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "삼성역");

        Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        // when
        stationDao.update(new StationEntity(stationId, "잠실역"));

        // then
        StationEntity result = jdbcTemplate.queryForObject("SELECT * FROM station WHERE id = ?", stationRowMapper,
                stationId);
        assertThat(result.getName()).isEqualTo("잠실역");
    }

    @Test
    void 삭제_테스트() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "삼성역");

        Long stationId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        // when
        stationDao.deleteById(stationId);

        // then
        List<StationEntity> result = jdbcTemplate.query("SELECT * FROM station WHERE id = ?", stationRowMapper,
                stationId);
        assertThat(result).isEmpty();
    }
}
