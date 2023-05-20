package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.Line;

@Component
public class LineDao {

    private static final RowMapper<Line> LINE_ROW_MAPPER = (rs, rowNum) ->
        Line.of(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            rs.getInt("additional_fee")
        );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("LINE")
            .usingGeneratedKeyColumns("id");
    }

    public Line insert(final Line line) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("additional_fee", line.getAdditionalFee());

        final Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return Line.of(lineId, line.getName(), line.getColor(), line.getAdditionalFee());
    }

    public Optional<Line> findById(Long id) {
        final String sql = "SELECT * FROM LINE WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, LINE_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Line> findAll() {
        final String sql = "SELECT * FROM LINE";
        return jdbcTemplate.query(sql, LINE_ROW_MAPPER);
    }

    public void update(final Line newLine) {
        final String sql = "UPDATE LINE SET name = ?, color = ?, additional_fee = ? WHERE id = ?";
        jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), newLine.getId(), newLine.getAdditionalFee());
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM LINE WHERE id = ?", id);
    }
}
