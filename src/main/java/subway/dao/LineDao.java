package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.Line;

import java.util.List;
import java.util.Optional;

@Component
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Line insert(final Line line) {
        final var sqlParameterSource = new BeanPropertySqlParameterSource(line);
        final Long lineId = insertAction.executeAndReturnKey(sqlParameterSource).longValue();

        return new Line(lineId, line.getName(), line.getColor());
    }

    public List<Line> findAll() {
        final String sql = "SELECT id, name, color FROM LINE";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Line> findById(final Long id) {
        final String sql = "SELECT * FROM line WHERE id = ?";
        final Line line = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new Line(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("color")
        ), id);

        return Optional.ofNullable(line);
    }

    public boolean hasNoStations(final Long lineId) {
        return true;
    }
}
