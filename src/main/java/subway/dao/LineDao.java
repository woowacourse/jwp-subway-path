package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
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
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final LineEntity line) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<LineEntity> findAll() {
        final String sql = "select id, name, color from line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(final Long id) {
        final String sql = "select id, name, color from line WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }


    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

    public Optional<LineEntity> findByName(final String name) {
        final String sql = "select id, name, color from line WHERE name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
