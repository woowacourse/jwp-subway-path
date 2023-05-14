package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.LineEntity;

import java.util.List;

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
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(LineEntity lineEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineEntity);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity findById(Long id) {
        String sql = "select id, name from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Long findIdByName(String name) {
        String sql = "select id from LINE WHERE name = ?";

        return jdbcTemplate.queryForObject(sql, Long.class, name);
    }
}
