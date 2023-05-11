package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class H2LineDao implements LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public H2LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public LineEntity insert(LineEntity lineEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", lineEntity.getId());
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, lineEntity.getName(), lineEntity.getColor());
    }

    @Override
    public List<LineEntity> findAll() {
        String sql = "SELECT id, name, color FROM line";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public LineEntity findById(Long id) {
        String sql = "SELECT id, name, color FROM line WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public void update(LineEntity newLineEntity) {
        String sql = "UPDATE line SET name = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(sql, newLineEntity.getName(), newLineEntity.getColor(), newLineEntity.getId());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM line WHERE id = ?", id);
    }
}
