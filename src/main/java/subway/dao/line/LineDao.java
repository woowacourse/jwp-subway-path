package subway.dao.line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("line_id"),
                    rs.getLong("line_number"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public boolean isExistLineByName(final String lineName) {
        String sql = "SELECT EXISTS(SELECT 1 FROM line WHERE name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, lineName));
    }

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("line_id");
    }

    public Long insert(final LineEntity lineEntity) {
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
}
