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
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(LineEntity lineEntity) {
        return insertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(lineEntity)).longValue();
    }

    public LineEntity findByName(String name) {
        final String sql = "SELECT * FROM line WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public LineEntity findById(Long id) {
        final String sql = "SELECT * FROM line WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public int deleteById(Long lineId) {
        final String sql = "DELETE FROM line WHERE id = ?";
        return jdbcTemplate.update(sql, lineId);
    }

    public boolean isExisted(String name) {
        final String sql = "SELECT EXISTS(SELECT * FROM line WHERE name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }
}
