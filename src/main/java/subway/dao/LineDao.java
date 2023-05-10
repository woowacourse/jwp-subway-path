package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

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
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("LINE")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(final LineEntity productEntity) {
        return simpleJdbcInsert.executeAndReturnKey(
                                       new BeanPropertySqlParameterSource(productEntity)
                               )
                               .longValue();
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
}
