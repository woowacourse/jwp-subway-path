package subway.adapter.out.persistence.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.adapter.out.persistence.entity.LineEntity;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getInt("surcharge")
            );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", lineEntity.getId());
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());
        params.put("surcharge", lineEntity.getSurcharge());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, lineEntity.getName(), lineEntity.getColor(), lineEntity.getSurcharge());
    }

    public List<LineEntity> findAll() {
        String sql = "SELECT id, name, color, surcharge FROM line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<LineEntity> findById(Long id) {
        String sql = "SELECT id, name, color, surcharge FROM line WHERE id = ?";

        try {
            LineEntity line = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.of(line);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<LineEntity> findByName(final String name) {
        String sql = "SELECT id, name, color, surcharge FROM line where name = ?";

        try {
            LineEntity line = jdbcTemplate.queryForObject(sql, rowMapper, name);
            return Optional.of(line);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(LineEntity newLineEntity) {
        String sql = "UPDATE line SET name = ?, color = ?, surcharge = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                new Object[]{newLineEntity.getName(), newLineEntity.getColor(), newLineEntity.getSurcharge(),
                        newLineEntity.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM line WHERE id = ?", id);
    }
}
