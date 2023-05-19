package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.subway.Line;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Line> rowMapper = (rs, rowNum) ->
        new Line(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color")
        );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("line")
            .usingGeneratedKeyColumns("id");
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor());
    }

    public List<Line> findAll() {
        String sql = "select * from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Line findById(Long id) {
        String sql = "select * from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), newLine.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

    public boolean checkExistenceByNameAndColor(String name, String color) {
        String sql = "select exists(select * from LINE WHERE name = ? AND color = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, boolean.class, name, color));
    }

    public boolean checkExistenceById(Long id) {
        String sql = "select exists(select * from LINE WHERE id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, boolean.class, id));
    }
}
