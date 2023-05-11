package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import subway.dao.entity.PathEntity;

import java.util.List;

@Component
public class PathDao {
    private final JdbcTemplate jdbcTemplate;

    public PathDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<PathEntity> findStationsByLineId(final Long lineId) {
        final String sql = "SELECT s.id AS station_id, name, up_station_id, down_station_id, distance " +
                "FROM station s " +
                "LEFT OUTER JOIN paths p " +
                "ON s.id = p.down_station_id " +
                "WHERE p.line_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new PathEntity(
                rs.getLong("station_id"),
                rs.getString("name"),
                rs.getLong("up_station_id"),
                rs.getInt("distance")), lineId);
    }

    public Long save(final PathEntity pathEntity) {
        return null;
    }

    public void deletePathByUpStationIdAndDownStationId(final Long id, final Long id1) {
    }
}
