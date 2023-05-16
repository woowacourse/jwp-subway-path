package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;

@Repository
public class LineDao {
    private static final int EXIST_NAME = 1;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(LineEntity lineEntity) {
        return insertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(lineEntity)).longValue();
    }

    public LineEntity findByName(final String name) {
        final String sql = "SELECT * FROM line WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public LineEntity findById(final Long id) {
        final String sql = "SELECT * FROM line WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int deleteById(final Long lineId) {
        final String sql = "DELETE FROM line WHERE id = ?";
        return jdbcTemplate.update(sql, lineId);
    }

    public boolean exists(final String lineName) {
        final String sql = "SELECT EXISTS(SELECT * FROM line WHERE name = ?)";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, lineName);

        return result == EXIST_NAME;
    }
}
