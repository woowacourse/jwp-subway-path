package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.entity.LineEntity;
import subway.exception.NoSuchLineException;
import subway.fixture.LineFixture.이호선;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
class LineEntityDaoTest {

    private final RowMapper<LineEntity> lineRowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("cost")
            );

    private final Map<String, Object> params = Map.of(
            "name", "2호선",
            "color", "GREEN",
            "cost", 0
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert simpleJdbcInsert;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new H2LineDao(jdbcTemplate, jdbcTemplate.getDataSource());
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
        jdbcTemplate.update("INSERT INTO line(name, color, cost) VALUES (?,?,?)", "2호선", "GREEN", 0);
        jdbcTemplate.update("INSERT INTO line(name, color, cost) VALUES (?,?,?)", "3호선", "ORANGE", 0);
        List<LineEntity> lineEntities = lineDao.findAll();
        assertAll(
                () -> assertThat(lineEntities)
                        .hasSize(2),
                () -> assertThat(lineEntities)
                        .extracting("name")
                        .containsExactlyInAnyOrder("2호선", "3호선")
        );
    }

    @Test
    void 아이디로_조회_테스트() {
        Long lineId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(() -> new NoSuchLineException(lineId));

        assertAll(
                () -> assertThat(lineEntity.getName()).isEqualTo("2호선"),
                () -> assertThat(lineEntity.getColor()).isEqualTo("GREEN")
        );
    }

    @Test
    void 갱신_테스트() {
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
        Long lineId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        lineDao.deleteById(lineId);

        List<LineEntity> result = jdbcTemplate.query("SELECT * FROM line WHERE id = ?", lineRowMapper, lineId);
        assertThat(result).isEmpty();
    }
}
