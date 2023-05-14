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
import subway.adapter.out.persistence.dao.LineDao;
import subway.adapter.out.persistence.entity.LineEntity;
import subway.fixture.LineFixture.이호선;

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
        LineEntity lineEntity = lineDao.insert(이호선.ENTITY);
        LineEntity result = jdbcTemplate.queryForObject("SELECT * FROM line WHERE id = ?", lineRowMapper,
                lineEntity.getId());
        assertThat(result).isNotNull();
    }

    @Test
    void 전체_조회_테스트() {
        jdbcTemplate.update("INSERT INTO line(name, color) VALUES (?,?)", "2호선", "GREEN");
        jdbcTemplate.update("INSERT INTO line(name, color) VALUES (?,?)", "3호선", "ORANGE");
        List<LineEntity> lineEntities = lineDao.findAll();
        assertAll(
                () -> assertThat(lineEntities.size())
                        .isEqualTo(2),
                () -> assertThat(lineEntities)
                        .extracting("name")
                        .containsExactlyInAnyOrder("2호선", "3호선")
        );
    }

    @Test
    void 갱신_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "GREEN");

        Long lineId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        lineDao.update(new LineEntity(lineId, "3호선", "ORANGE"));

        LineEntity result = jdbcTemplate.queryForObject("SELECT * FROM line WHERE id = ?", lineRowMapper, lineId);
        assertAll(
                () -> assertThat(result.getName()).isEqualTo("3호선"),
                () -> assertThat(result.getColor()).isEqualTo("ORANGE")
        );
    }

    @Test
    void 삭제_테스트() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "2호선");
        params.put("color", "GREEN");

        Long lineId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        lineDao.deleteById(lineId);

        List<LineEntity> result = jdbcTemplate.query("SELECT * FROM line WHERE id = ?", lineRowMapper, lineId);
        assertThat(result).isEmpty();
    }
}
