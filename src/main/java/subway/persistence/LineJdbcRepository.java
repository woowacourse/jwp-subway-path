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
    private static final int UPDATED_COUNT = 1;
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
    public List<Line> findAll() {
        String sql = "SELECT * FROM line";
        return jdbcTemplate.query(sql, lineRowMapper);
    }

    @Override
    public Line findById(final Long lineId) {
        String sql = "SELECT * FROM line WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, lineRowMapper, lineId);
    }

    @Override
    public boolean updateLine(final long lineId, final Line line) {
        final String sql = "UPDATE line SET name = ? WHERE id = ?";
        final int updateCount = jdbcTemplate.update(sql, line.getName(), lineId);

        return updateCount == UPDATED_COUNT;
    }

    @Override
    public Line findByName(final String lineName) {
        final String sql = "SELECT * FROM line WHERE name = ?";
        final List<Line> lines = jdbcTemplate.query(sql, lineRowMapper, lineName);

        if(lines.isEmpty()){
            throw new IllegalArgumentException("존재하지 않는 노선입니다");
        }

        return lines.get(0);
    }

    @Override
    public boolean deleteById(final Long lineId) {
        String sql = "DELETE FROM line WHERE id = ?";
        final int deleteCount = jdbcTemplate.update(sql, lineId);

        return deleteCount == DELETED_COUNT;
    }
}
