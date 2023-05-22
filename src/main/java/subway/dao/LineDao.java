package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

@Repository
public class LineDao {

    private static final RowMapper<LineEntity> ROW_MAPPER = (rs, rowNum) -> new LineEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            rs.getInt("fare")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingColumns("name", "color", "fare")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity save(final LineEntity lineEntity) {
        final SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(lineEntity);
        final Long lineId = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new LineEntity(lineId, lineEntity.getName(), lineEntity.getColor(), lineEntity.getFare());
    }

    public Optional<LineEntity> findById(final Long lineId) {
        final String sql = "SELECT id, name, color, fare FROM line WHERE id = ?";
        try {
            final LineEntity result = jdbcTemplate.queryForObject(
                    sql,
                    ROW_MAPPER,
                    lineId
            );
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<LineEntity> findAll() {
        final String sql = "SELECT id, name, color, fare FROM line";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public int update(final LineEntity lineEntity) {
        final String sql = "UPDATE line SET name = ?, color = ?, fare = ? WHERE id = ?";
        return jdbcTemplate.update(
                sql,
                lineEntity.getName(), lineEntity.getColor(), lineEntity.getFare(), lineEntity.getId()
        );
    }
}
