package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.row.LinePropertyRow;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LinePropertyDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LinePropertyRow> rowMapper = (rs, rowNum) ->
            new LinePropertyRow(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LinePropertyDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line_property")
                .usingGeneratedKeyColumns("id");
    }

    public LinePropertyRow insert(LinePropertyRow row) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", row.getId());
        params.put("name", row.getName());
        params.put("color", row.getColor());
        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LinePropertyRow(lineId, row.getName(), row.getColor());
    }

    public List<LinePropertyRow> selectAll() {
        String sql = "select id, name, color from line_property";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LinePropertyRow findById(Long id) {
        String sql = "select id, name, color from line_property WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(LinePropertyRow row) {
        String sql = "update line_property set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{row.getName(), row.getColor(), row.getId()});
    }

    public void deleteById(Long id) {
        String sql = "delete from line_property where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
