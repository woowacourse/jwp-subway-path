package subway.dao.line;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("line_id"),
                    rs.getLong("line_number"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource, final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("line_id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public long insert(final LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineEntity.getName());
        params.put("line_number", lineEntity.getLineNumber());
        params.put("color", lineEntity.getColor());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT line_id, line_number, name, color FROM line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity findByName(final String name) {
        String sql = "SELECT line_id, line_number, name, color FROM line WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM line WHERE line_id = ?", id);
    }

    public LineEntity findByLineNumber(final Long lineNumber) {
        String sql = "SELECT line_id, line_number, name, color FROM line WHERE line_number = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, lineNumber);
    }

    public Optional<LineEntity> findById(final Long id) {
        String sql = "SELECT line_id, line_number, name, color FROM line WHERE line_id = :line_id";
        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource("line_id", id), rowMapper).stream()
                .findAny();
    }

    public void updateLine(final long lineId, final LineEntity lineEntity) {
        String sql = "UPDATE line SET line_number = ?, name = ?, color = ? WHERE line_id = ?";
        jdbcTemplate.update(sql, lineEntity.getLineNumber(), lineEntity.getName(), lineEntity.getColor(), lineId);
    }
}
