package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import subway.domain.Path;
import subway.domain.Station;
import subway.persistence.dao.entity.PathEntity;

import java.util.List;
import java.util.Map;

@Component
public class PathDao {

    private static final RowMapper mapper = (rs, rowNum) -> new PathEntity(rs.getLong("id"), rs.getLong("up_station_id"), rs.getLong("down_station_id"), rs.getLong("line_id"), rs.getInt("distance"));

    private final JdbcTemplate jdbcTemplate;

    public PathDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<PathEntity> findAllByLineId(final Long lineId) {
        final String sql = "SELECT * FROM paths WHERE line_id = ?";
        return jdbcTemplate.query(sql, mapper, lineId);
    }

    public List<PathEntity> findAll() {
        final String sql = "SELECT * FROM paths";
        return jdbcTemplate.query(sql, mapper);
    }

    public void addAll(final Long lineId, final Map<Station, Path> paths, List<Station> stations) {
        final String sql = "INSERT INTO paths (line_id, up_station_id, down_station_id, distance) " +
                "VALUES (? ,? ,? ,?)";
        stations.remove(stations.size() - 1);
        jdbcTemplate.batchUpdate(sql, stations, 100, (ps, station) -> {
            ps.setLong(1, lineId);
            ps.setLong(2, station.getId());
            ps.setLong(3, paths.get(station).getNext().getId());
            ps.setInt(4, paths.get(station).getDistance());
        });
    }

    public void deleteByLineId(final Long id) {
        final String sql = "DELETE FROM paths WHERE line_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
