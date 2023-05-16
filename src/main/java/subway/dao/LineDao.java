package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Map<String, Object> params = new HashMap<>();
        params.put("id", lineEntity.getId());
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, lineEntity.getName(), lineEntity.getColor());
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity findById(final Long id) {
        try {
            String sql = "select id, name, color from LINE WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new IllegalArgumentException("존재하지 않는 호선입니다.");
        }
    }

    public void update(final LineEntity lineEntity) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{lineEntity.getName(), lineEntity.getColor(), lineEntity.getId()});
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
