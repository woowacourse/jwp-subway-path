package subway.dao.line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

@Component
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("line_id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("extra_fare")
            );

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("line_id");
    }

    public LineEntity insert(final LineEntity line) {
        final Map<String, Object> params = new HashMap<>();
        params.put("line_id", line.getLineId());
        params.put("name", line.getName());
        params.put("color", line.getColor());

        final Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, line.getName(), line.getColor());
    }

    public List<LineEntity> findAll() {
        final String sql = "select line_id, name, color, extra_fare from line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity findById(final Long id) {
        final String sql = "select line_id, name, color, extra_fare from line WHERE line_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
}
