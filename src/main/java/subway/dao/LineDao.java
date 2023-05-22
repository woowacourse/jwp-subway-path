package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;

@Repository
public class LineDao {
    
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );
    
    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }
    
    public Line insert(final Line line) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        
        final Long lineId = this.insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor());
    }
    
    public List<Line> findAll() {
        final String sql = "select id, name, color from LINE";
        return this.jdbcTemplate.query(sql, this.rowMapper);
    }
    
    public Line findById(final Long id) {
        final String sql = "select id, name, color from LINE WHERE id = ?";
        return this.jdbcTemplate.queryForObject(sql, this.rowMapper, id);
    }
    
    public void update(final Line newLine) {
        final String sql = "update LINE set name = ?, color = ? where id = ?";
        this.jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), newLine.getId());
    }
    
    public void deleteById(final Long id) {
        this.jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
