package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.entity.LineEntity;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName().getName());
        params.put("color", line.getColor().getColor());

        long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, line.getName().getName(), line.getColor().getColor());
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, newLine.getName().getName(), newLine.getColor().getColor(), newLine.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM Line WHERE id = ?";
        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class, id);

        if (integer == null) {
            return false;
        }

        return integer > 0;
    }
}
