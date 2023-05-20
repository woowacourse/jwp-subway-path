package subway.line.domain.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.line.domain.entity.LineEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            LineEntity.of(
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

    public Long insert(final LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", lineEntity.getId());
        params.put("name", lineEntity.getNameValue());
        params.put("color", lineEntity.getColorValue());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(final Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateById(final Long id, final LineEntity lineEntity) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{lineEntity.getNameValue(), lineEntity.getColorValue(), id});
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from LINE where id = ?", id);
    }

}
