package subway.adapter.out.persistence.dao;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.adapter.out.persistence.entity.LineEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Optional<LineEntity> findById(final Long lineId) {
        String sql = "select * from line where id = ?";

        try {
            LineEntity findByLine = jdbcTemplate.queryForObject(sql, lineRowMapper, lineId);
            return Optional.of(findByLine);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }
}
