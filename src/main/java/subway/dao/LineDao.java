package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;

@Repository
public class LineDao {
    private static final RowMapper<LineEntity> ENTITY_MAPPER = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"));

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert insertAction;

    @Autowired
    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(final String name, final String color) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Optional<LineEntity> findByName(final String name) {
        final String sql = "SELECT id, name, color FROM line where name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ENTITY_MAPPER, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findById(final long id) {
        final String sql = "SELECT id, name, color FROM line where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ENTITY_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT id, name, color FROM line";
        return jdbcTemplate.query(sql, ENTITY_MAPPER);
    }
}
