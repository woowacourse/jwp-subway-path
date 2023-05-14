package subway.persistence;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.Line;
import subway.domain.repository.LineRepository;

@Repository
public class LineJdbcRepository implements LineRepository {

    private static final int DELETED_COUNT = 1;
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    private final RowMapper<Line> lineRowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public LineJdbcRepository(final JdbcTemplate jdbcTemplate) {
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public long createLine(final Line line) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(line);
        return insert.executeAndReturnKey(params).longValue();
    }

    @Override
    public boolean deleteById(final Long lineIdRequest) {
        String sql = "delete from line where id = ?";
        final int deleteCount = jdbcTemplate.update(sql, lineIdRequest);

        return deleteCount == DELETED_COUNT;
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
