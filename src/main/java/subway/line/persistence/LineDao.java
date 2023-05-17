package subway.line.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class LineDao {

    private static final RowMapper<LineEntity> LINE_ENTITY_ROW_MAPPER = (rs, rowNum) -> {
        final long id = rs.getLong("id");
        final String findName = rs.getString("line_name");
        return new LineEntity(id, findName);
    };
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("LINE")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(final LineEntity lineEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(lineEntity))
            .longValue();
    }

    public Optional<LineEntity> findById(final Long id) {
        final String sql = "SELECT * FROM LINE WHERE id = :id";
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);

        final List<LineEntity> findLine = namedParameterJdbcTemplate.query(sql, params, LINE_ENTITY_ROW_MAPPER);
        return findLine.stream().findAny();
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT * FROM LINE";
        return jdbcTemplate.query(sql, LINE_ENTITY_ROW_MAPPER);
    }

    public Optional<LineEntity> findByName(String name) {
        final String sql = "SELECT * FROM LINE WHERE line_name = :name";
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);

        final List<LineEntity> findLine = namedParameterJdbcTemplate.query(sql, params, LINE_ENTITY_ROW_MAPPER);
        return findLine.stream().findAny();
    }
}
