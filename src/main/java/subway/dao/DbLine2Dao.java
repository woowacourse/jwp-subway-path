package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class DbLine2Dao {
    // 이걸 씀

    public static final RowMapper<Line> LINE_ROW_MAPPER = (resultSet, rowNum) -> new Line(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertStation;

    public DbLine2Dao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertStation = new SimpleJdbcInsert(dataSource)
                .withTableName("line2")
                .usingGeneratedKeyColumns("id");
    }

    public Line saveLine(Line line) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(line);
        final long newId = insertStation.executeAndReturnKey(parameters).longValue();
        return new Line(newId, line.getName());
    }

    public List<Line> findAll() {
        final String sql = "SELECT * FROM line2";
        return jdbcTemplate.query(sql, LINE_ROW_MAPPER);
    }

    public Line findById(final Long id) {
        final String sql = "SELECT * FROM line2 WHERE id = :id";
        final Map<String, Long> parameters = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, LINE_ROW_MAPPER);
    }

    public Line findByName(final String name) {
        final String sql = "SELECT * FROM line2 WHERE name = :name";
        final Map<String, String> parameters = Map.of("name", name);
        return namedParameterJdbcTemplate.queryForObject(sql, parameters, LINE_ROW_MAPPER);
    }
}
