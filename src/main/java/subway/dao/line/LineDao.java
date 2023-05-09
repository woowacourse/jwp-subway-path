package subway.dao.line;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("line_id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public boolean isExistLineByName(final String lineName) {
        String sql = "SELECT EXISTS(SELECT 1 FROM line WHERE name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, lineName));
    }

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("line_id");
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor());
    }

    public List<Line> findAll() {
        String sql = "select line_id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Line findByName(final String name) {
        String sql = "select line_id, name, color from LINE WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public Line findById(Long id) {
        String sql = "select line_id, name, color from LINE WHERE line_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ? where line_id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where line_id = ?", id);
    }
}
