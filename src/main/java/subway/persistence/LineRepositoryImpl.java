package subway.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.repository.LineRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineRepositoryImpl implements LineRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    private final RowMapper<Line> lineRowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public LineRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createLine(final Line line) {
        final Map<String, Object> parameters = new HashMap<>();

        parameters.put("name", line.getName());

        insert.execute(parameters);
    }

    @Override
    public void deleteById(final Long lineIdRequest) {
        String sql = "delete from line where id = ?";
        jdbcTemplate.update(sql, lineIdRequest);
    }

    @Override
    public List<Line> findAll() {
        String sql = "select * from line";
        return jdbcTemplate.query(sql, lineRowMapper);
    }

    @Override
    public Line findById(final Long lineIdRequest) {
        String sql = "select * from line where id = ?";
        return jdbcTemplate.queryForObject(sql, lineRowMapper, lineIdRequest);
    }
}
