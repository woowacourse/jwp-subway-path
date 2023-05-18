package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public LineEntity insert(LineEntity line) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("upbound_station_id", line.getUpBoundStationId());
        params.put("downbound_station_id", line.getDownBoundStationId());

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new LineEntity(lineId, line.getName(), line.getColor(), line.getUpBoundStationId(), line.getDownBoundStationId());
    }

    public List<LineEntity> findAll() {
        String sql = "select id, name, color, upbound_station_id, downbound_station_id from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public boolean existsById(Long id) {
        String sql = "select count(*) from LINE where id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, id) == 1;
    }

    public LineEntity update(LineEntity lineEntity) {
        String sql = "update LINE set name = ?, color = ?, upbound_station_id = ?, downbound_station_id =? where id = ?";
        jdbcTemplate.update(sql, lineEntity.getName(), lineEntity.getColor(), lineEntity.getUpBoundStationId(), lineEntity.getDownBoundStationId(), lineEntity.getId());

        return new LineEntity(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), lineEntity.getUpBoundStationId(), lineEntity.getDownBoundStationId());
    }

    public void deleteById(Long lineId) {
        String sql = "DELETE FROM LINE WHERE id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public Optional<LineEntity> findById(Long lineId) {
        String sql = "SELECT id, name, color, upbound_station_id, downbound_station_id FROM LINE WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByName(String name) {
        String sql = "select count(*) from LINE where name = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, name) >= 1;
    }
}
