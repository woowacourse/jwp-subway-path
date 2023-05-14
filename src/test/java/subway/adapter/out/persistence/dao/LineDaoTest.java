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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.adapter.out.persistence.entity.LineEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    private LineDao lineDao;

    private final RowMapper<LineEntity> lineRowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 삽입_테스트() {
        // given
        LineEntity entity = new LineEntity("2호선", "GREEN");

        // when
        LineEntity response = lineDao.insert(entity);

        // then
        String sql = "SELECT * FROM line WHERE id = ?";
        LineEntity result = jdbcTemplate.queryForObject(sql, lineRowMapper, response.getId());
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(entity);
    }

    @Test
    void 전체_조회_테스트() {
        // given
        jdbcTemplate.update("INSERT INTO line(name, color) VALUES (?,?)", "2호선", "GREEN");
        jdbcTemplate.update("INSERT INTO line(name, color) VALUES (?,?)", "3호선", "ORANGE");

        // when
        List<LineEntity> lineEntities = lineDao.findAll();

        // then
        assertAll(
                () -> assertThat(lineEntities.size())
                        .isEqualTo(2),
                () -> assertThat(lineEntities)
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .ignoringCollectionOrder()
                        .isEqualTo(List.of(
                                new LineEntity("2호선", "GREEN"),
                                new LineEntity("3호선", "ORANGE")))
        );
    }

    @Test
    void 아이디로_조회_테스트() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "GREEN");
        Long lineId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        // when
        Optional<LineEntity> line = lineDao.findById(lineId);

        // then
        assertAll(
                () -> assertThat(line).isPresent(),
                () -> assertThat(line.get())
                        .usingRecursiveComparison()
                        .isEqualTo(new LineEntity(lineId, "2호선", "GREEN"))
        );
    }

    @Test
    void 아이디로_조회시_노선이_존재하지않을떼_Optional_empty_반환() {
        // when
        Optional<LineEntity> line = lineDao.findById(-1L);

        // then
        assertThat(line).isEmpty();
    }

    @Test
    void 갱신_테스트() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "GREEN");
        Long lineId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        // when
        lineDao.update(new LineEntity(lineId, "3호선", "ORANGE"));

        // then
        String sql = "SELECT * FROM line WHERE id = ?";
        LineEntity result = jdbcTemplate.queryForObject(sql, lineRowMapper, lineId);
        assertThat(result)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new LineEntity("3호선", "ORANGE"));
    }

    @Test
    void 삭제_테스트() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "GREEN");
        Long lineId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        // when
        lineDao.deleteById(lineId);

        // then
        String sql = "SELECT * FROM line WHERE id = ?";
        List<LineEntity> result = jdbcTemplate.query(sql, lineRowMapper, lineId);
        assertThat(result).isEmpty();
    }
}
