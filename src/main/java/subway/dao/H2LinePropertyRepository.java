package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.LineProperty;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class H2LinePropertyRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineProperty> rowMapper = (rs, rowNum) ->
            new LineProperty(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public H2LinePropertyRepository(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line_property")
                .usingGeneratedKeyColumns("id");
    }

    public LineProperty insert(LineProperty lineProperty) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", lineProperty.getId());
        params.put("name", lineProperty.getName());
        params.put("color", lineProperty.getColor());
        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineProperty(lineId, lineProperty.getName(), lineProperty.getColor());
    }

    public List<LineProperty> findAll() {
        String sql = "select id, name, color from line_property";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineProperty findById(Long id) {
        String sql = "select id, name, color from line_property WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(LineProperty lineProperty) {
        String sql = "update line_property set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{lineProperty.getName(), lineProperty.getColor(), lineProperty.getId()});
    }

    public void deleteById(Long id) {
        String sql = "delete from line_property where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
