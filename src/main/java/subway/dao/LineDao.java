package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.LineEntity;
import subway.domain.line.Line;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private static final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("LINE")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(Line line) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(line);
        return insertAction.executeAndReturnKey(sqlParameterSource).longValue();
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Long id, Line line) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, line.getName(), line.getColor(), id);
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from LINE where id = ?", id);
    }

    public boolean existsByName(String lineName) {
        String sql = "SELECT COUNT(*) FROM LINE WHERE name = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, lineName) > 0;
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM LINE WHERE id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, id) > 0;
    }

    public boolean doesNotExistById(Long id) {
        return !existsById(id);
    }
}
