package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    final RowMapper<Line> lineRowMapper = (result, count) ->
            new Line(result.getLong("id"),
                    result.getString("name")
            );

    public Line insert(final Line line) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", line.getName());
        final Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Line(id, line.getName());
    }

    public Optional<Line> findById(final Long lineId) {
        final String sql = "SELECT l.id, l.name FROM line l WHERE l.id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, lineRowMapper, lineId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Line> findByName(final Line line) {
        final String sql = "SELECT l.id, l.name FROM line l WHERE l.name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, lineRowMapper, line.getName()));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Line> findAll() {
        final String sql = "SELECT l.id, l.name FROM line l ORDER BY l.id";
        return jdbcTemplate.query(sql, lineRowMapper);
    }
}