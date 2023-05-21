package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("surcharge")
            );

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(final LineEntity line) {
        final BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(line);
        Long lineId = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new LineEntity(lineId, line.getName(), line.getColor(), line.getSurcharge());
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT * FROM LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(final Long id) {
        String sql = "SELECT * FROM LINE WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByName(final String name) {
        String sql = "SELECT * FROM LINE WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final LineEntity newLine) {
        String sql = "UPDATE LINE SET name = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), newLine.getId());
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM Line WHERE id = ?", id);
    }
}
