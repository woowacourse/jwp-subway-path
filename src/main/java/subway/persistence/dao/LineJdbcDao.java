package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.LineEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LineJdbcDao implements LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    private final RowMapper<LineEntity> lineRowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public LineJdbcDao(final JdbcTemplate jdbcTemplate) {
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createLine(final LineEntity lineEntity) {
        final Map<String, Object> parameters = new HashMap<>();

        parameters.put("name", lineEntity.getName());

        return insert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public void deleteById(final Long lineIdRequest) {
        String sql = "delete from line where id = ?";
        jdbcTemplate.update(sql, lineIdRequest);
    }

    @Override
    public List<LineEntity> findAll() {
        String sql = "select * from line";
        return jdbcTemplate.query(sql, lineRowMapper);
    }

    @Override
    public LineEntity findById(final Long lineIdRequest) {
        String sql = "select * from line where id = ?";
        return jdbcTemplate.queryForObject(sql, lineRowMapper, lineIdRequest);
    }
}
