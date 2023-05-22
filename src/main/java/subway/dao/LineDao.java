package subway.dao;

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

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("line_id");
    }

    public boolean isLineNumberExist(final Long lineNumber) {
        String sql = "SELECT EXISTS(SELECT 1 FROM line WHERE line_number = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, lineNumber));
    }

    public boolean isLineIdExist(final Long lineId) {
        String sql = "SELECT EXISTS(SELECT 1 FROM line WHERE line_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, lineId));
    }

    public Long save(final LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineEntity.getName());
        params.put("line_number", lineEntity.getLineNumber());
        params.put("color", lineEntity.getColor());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public LineEntity findByLineNumber(final Long lineNumber) {
        String sql = "SELECT line_id, line_number, name, color FROM line WHERE line_number = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, lineNumber);
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT line_id, line_number, name, color FROM line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteByLineId(final Long lineId) {
        jdbcTemplate.update("DELETE FROM line WHERE line_id = ?", lineId);
    }
}
