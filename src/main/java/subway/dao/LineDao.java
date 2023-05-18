package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.LineEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class LineDao implements Dao<LineEntity> {

    private static final RowMapper<LineEntity> lineRowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long insert(final LineEntity line) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    @Override
    public Optional<LineEntity> findById(final Long id) {
        final String sql = "SELECT id, name, color FROM line WHERE id = ?";
        return findInOptional(jdbcTemplate, sql, lineRowMapper, id);
    }

    @Override
    public List<LineEntity> findAll() {
        final String sql = "SELECT id, name, color FROM line";
        return jdbcTemplate.query(sql, lineRowMapper);
    }

    @Override
    public void update(final LineEntity lineEntity) {
        final String sql = "UPDATE line SET name = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(sql, lineEntity.getName(), lineEntity.getColor(), lineEntity.getId());
    }


    @Override
    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM Line WHERE id = ?", id);
    }

    public Optional<LineEntity> findByName(final String name) {
        final String sql = "SELECT id, name, color FROM line WHERE name = ?";
        return findInOptional(jdbcTemplate, sql, lineRowMapper, name);
    }
}
