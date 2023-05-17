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
import subway.entity.LineEntity;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("lines")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final LineEntity lineEntity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", lineEntity.getId());
        parameters.put("name", lineEntity.getName());
        parameters.put("color", lineEntity.getColor());

        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT id, name, color FROM LINES";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(final Long id) {
        String sql = "SELECT id, name, color FROM LINES WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByName(final String lineName) {
        String sql = "SELECT id, name, color FROM LINES WHERE name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineName));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateById(final LineEntity line) {
        String sql = "UPDATE LINES set name = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(sql, line.getName(), line.getColor(), line.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM Lines WHERE id = ?", id);
    }
}
