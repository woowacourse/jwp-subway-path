package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert =
                new SimpleJdbcInsert(jdbcTemplate)
                        .withTableName("line")
                        .usingGeneratedKeyColumns("id");
    }

    public Optional<LineEntity> findLineByName(final String lineName) {
        final String sql = "SELECT * FROM line l WHERE l.name = ?";

        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        sql,
                        rowMapper,
                        lineName)
        );
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT * FROM line";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteLineById(final Long lineId) {
        final String sql = "DELETE FROM line l WHERE l.id = ?";

        jdbcTemplate.update(sql, lineId);
    }

    public Long save(final LineEntity lineEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(lineEntity))
                               .longValue();
    }
}
