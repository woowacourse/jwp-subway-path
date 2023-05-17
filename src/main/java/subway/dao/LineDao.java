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
            new LineEntity.Builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .build();

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", lineEntity.getName());
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Optional<LineEntity> findById(long id) {
        String sql = "select id, name from LINE WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Long> findIdByName(String name) {
        String sql = "select id from LINE where name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, new Object[]{name}, Long.class));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
