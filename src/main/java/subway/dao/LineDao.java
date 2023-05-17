package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.domain.line.LineColor;
import subway.domain.line.LineName;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    new LineName(rs.getString("name")),
                    new LineColor(rs.getString("color"))
            );

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("lines")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final Line line) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", line.getId());
        parameters.put("name", line.getLineName().name());
        parameters.put("color", line.getLineColor().color());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public List<Line> findAll() {
        String sql = "SELECT id, name, color FROM LINES";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Line> findById(Long id) {
        String sql = "SELECT id, name, color FROM LINES WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Line> findByName(final LineName lineName) {
        String sql = "SELECT id, name, color FROM LINES WHERE name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineName.name()));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateById(Line line) {
        String sql = "UPDATE LINES set name = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(sql, line.getLineName().name(), line.getLineColor().color(), line.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM Lines WHERE id = ?", id);
    }

}
