package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<LineEntity> findLineByName(final String lineName) {
        final String sql = "SELECT * FROM LINE L WHERE L.name = ?";

        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        sql,
                        rowMapper,
                        lineName)
        );
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT * FROM LINE";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteLineById(final Long lineId) {
        final String sql = "DELETE FROM LINE L WHERE L.id = ?";

        jdbcTemplate.update(sql, lineId);
    }
}
