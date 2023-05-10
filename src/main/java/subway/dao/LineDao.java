package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Line> rowMapper = (rs, rowNum) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color"),
                    rs.getLong("up_endpoint_id"),
                    rs.getLong("down_endpoint_id")
            );

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Line insert(final Line line) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());

        final Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor(), null, null);
    }

    public List<Line> findAll() {
        final String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Line findById(final Long id) {
        final String sql = "select * from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Line findByName(final String name) {
        final String sql = "select * from LINE WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public void update(final Long id, final Line newLine) {
        final String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), id);
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

    public void updateUpEndpoint(final Long lineId, final Long stationId) {
        final String sql = "update LINE set up_endpoint_id = ? where id = ?";
        jdbcTemplate.update(sql, stationId, lineId);

    }

    public void updateDownEndpoint(final Long lineId, final Long stationId) {
        final String sql = "update LINE set down_endpoint_id = ? where id = ?";
        jdbcTemplate.update(sql, stationId, lineId);
    }
}
