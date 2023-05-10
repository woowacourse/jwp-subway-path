package subway.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingColumns("name", "color")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity save(final LineEntity entity) {
        final SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(entity);
        final Long id = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new LineEntity(id, entity.getName(), entity.getColor());
    }

    public Optional<LineEntity> findById(final Long lineId) {
        final String sql = "SELECT id, name, color FROM line WHERE id = ?";
        try {
            final LineEntity result = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new LineEntity(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("color")
                    ),
                    lineId
            );
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
