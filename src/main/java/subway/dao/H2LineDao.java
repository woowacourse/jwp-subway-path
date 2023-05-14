package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.Entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class H2LineDao implements LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            LineEntity.of(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")

            );

    public H2LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public LineEntity insert(LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", lineEntity.getId());
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());

        long lineId = insertAction.executeAndReturnKey(params).longValue();
        return LineEntity.of(lineId, lineEntity.getName(), lineEntity.getColor());
    }

    @Override
    public List<LineEntity> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<LineEntity> findById(long id) {
        try {
            String sql = "select id, name, color from LINE WHERE id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public int update(LineEntity lineEntity) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        return jdbcTemplate.update(sql, lineEntity.getName(), lineEntity.getColor(), lineEntity.getId());
    }

    @Override
    public int deleteById(long id) {
        return jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
