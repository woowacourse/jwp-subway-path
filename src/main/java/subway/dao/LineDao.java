package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    private RowMapper<LineEntity> rowMapper = (resultSet, rowNumber) -> new LineEntity(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("color")
    );

    public Long save(final LineEntity lineEntity) {
        return insertAction.executeAndReturnKey(
                        new MapSqlParameterSource("name", lineEntity.getName())
                                .addValue("color", lineEntity.getColor()))
                .longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(Long id) {
        String sql = "select id, name, color from line WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<LineEntity> findByNameOrColor(final LineEntity lineEntity) {
        String sql = "SELECT * FROM line WHERE name = ? OR color = ?";
        return jdbcTemplate.query(sql, rowMapper, lineEntity.getName(), lineEntity.getColor());
    }

    public void update(final LineEntity lineEntity) {
        final String sql = "UPDATE line SET name = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(sql, lineEntity.getName(), lineEntity.getColor(), lineEntity.getId());
    }

    public void delete(LineEntity lineEntity) {
        final String sql = "DELETE FROM line WHERE id = ?";
        jdbcTemplate.update(sql, lineEntity.getId());
    }
}
