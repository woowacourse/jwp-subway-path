package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(final LineEntity lineEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", lineEntity.getId());
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());

        final Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, lineEntity.getName(), lineEntity.getColor());
    }

    public Optional<List<LineEntity>> findAll() {
        try {
            final String sql = "select id, name, color from LINE";
            return Optional.of(jdbcTemplate.query(sql, rowMapper));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findById(final Long id) {
        try {
            final String sql = "select id, name, color from LINE WHERE id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByName(final String name) {
        try {
            final String sql = "select id, name, color from LINE WHERE name = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final LineEntity newLineEntity) {
        final String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, newLineEntity.getName(), newLineEntity.getColor(), newLineEntity.getId());
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
