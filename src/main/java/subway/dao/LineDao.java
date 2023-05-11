package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.entity.LineEntity;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<LineEntity> rowMapper = (rs, rowNum) ->
        new LineEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            rs.getLong("upbound_station_id"),
            rs.getLong("downbound_station_id")
        );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("line")
            .usingGeneratedKeyColumns("id");
    }

    public LineEntity insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("upbound_station_id", null);
        params.put("downbound_station_id", null);

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, line.getName(), line.getColor(), null, null);
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public LineEntity findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Optional<LineEntity> findByName(final String name) {
        String sql = "SELECT id, name, color, upbound_station_id, downbound_station_id FROM LINE WHERE name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateBoundStations(LineEntity newLine) {
        String sql = "update LINE set  upbound_station_id= ?, downbound_station_id = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getUpBoundStationId(), newLine.getDownBoundStationId(), newLine.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
