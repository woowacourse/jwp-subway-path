package subway.infrastructure.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.infrastructure.entity.LineRow;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineRow> rowMapper = (rs, rowNum) ->
            new LineRow(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineRow insert(LineRow row) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", row.getId());
        params.put("name", row.getName());
        params.put("color", row.getColor());
        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineRow(lineId, row.getName(), row.getColor());
    }

    public List<LineRow> selectAll() {
        String sql = "select id, name, color from line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineRow findById(Long id) {
        String sql = "select id, name, color from line WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(LineRow row) {
        String sql = "update line set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{row.getName(), row.getColor(), row.getId()});
    }

    public void deleteById(Long id) {
        String sql = "delete from line where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
