package subway.dao;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.Line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("additionalFare")
            );

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Line insert(final Line line) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("additionalFare", line.getAdditionalFare());

        try {
            final Long lineId = insertAction.executeAndReturnKey(params).longValue();

            return new Line(lineId, line.getName(), line.getColor(), line.getAdditionalFare());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("노선 이름은 중복될 수 없습니다.");
        }
    }

    public List<Line> findAll() {
        final String sql = "SELECT id, name, color, additionalFare FROM line";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Line findById(final Long id) {
        try {
            final String sql = "SELECT id, name, color, additionalFare FROM line WHERE id = ?";

            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new IllegalArgumentException("해당 노선이 존재하지 않습니다.");
        }
    }

    public void update(final Line line) {
        final String sql = "UPDATE line SET name = ?, color = ?, additionalFare = ? WHERE id = ?";

        jdbcTemplate.update(sql, line.getName(), line.getColor(), line.getAdditionalFare(), line.getId());
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM line WHERE id = ?", id);
    }
}
