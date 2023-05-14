package subway.line.adapter.output.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
            new LineEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );
    
    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }
    
    public Long insert(final LineEntity lineEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<LineEntity> findAll() {
        final String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

//    public Line findById(Long id) {
//        String sql = "select id, name, color from LINE WHERE id = ?";
//        return jdbcTemplate.queryForObject(sql, rowMapper, id);
//    }
//
//    public void update(Line newLine) {
//        String sql = "update LINE set name = ?, color = ? where id = ?";
//        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getId()});
//    }
//
//    public void deleteById(Long id) {
//        jdbcTemplate.update("delete from Line where id = ?", id);
//    }
}
