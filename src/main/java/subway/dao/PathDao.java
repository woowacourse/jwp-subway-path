package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.dao.entity.PathEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PathDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public PathDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("paths")
                .usingGeneratedKeyColumns("id");
    }


    public List<PathEntity> findPathsByLineId(final Long lineId) {
        final String sql = "SELECT s.id AS station_id, name, up_station_id, down_station_id, distance " +
                "FROM station s " +
                "LEFT OUTER JOIN paths p " +
                "ON s.id = p.down_station_id " +
                "WHERE p.line_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new PathEntity(
                rs.getLong("station_id"),
                rs.getString("name"),
                rs.getLong("up_station_id"),
                rs.getLong("distance")), lineId);
    }

    public Long save(final PathEntity pathEntity, final Long lineId) {
        Map<String, Long> params = new HashMap<>();
        params.put("line_id", lineId);
        params.put("up_station_id", pathEntity.getUpStationId());
        params.put("down_station_id", pathEntity.getStationId());
        params.put("distance", pathEntity.getDistance());
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return id;
//        final String sql = "INSERT INTO paths (line_id,up_station_id,down_station_id,distance) VALUES (?,?,?,?)";
//        final KeyHolder keyHolder = new GeneratedKeyHolder();
//        jdbcTemplate.update(connection -> {
//            final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
//            ps.setLong(1, lineId);
//            ps.setLong(2, pathEntity.getUpStationId());
//            ps.setLong(3, pathEntity.getStationId());
//            ps.setLong(4, pathEntity.getDistance());
//            return ps;
//        }, keyHolder);
//        return (long) keyHolder.getKeys().get("id");
    }

    public void deletePathByUpStationIdAndDownStationId(final Long upStationId, final Long downStationId) {
        String sql = "DELETE FROM paths WHERE up_station_id = ? AND down_station_id = ?";
        jdbcTemplate.update(sql, upStationId, downStationId);
    }
}
