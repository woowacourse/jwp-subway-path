package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import subway.domain.Station;
import subway.domain.path.Path;
import subway.domain.path.Paths;

import java.util.List;

@Component
public class PathDao {
    private final JdbcTemplate jdbcTemplate;

    public PathDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Path> rowMapper = (rs, rowNum) -> {
        final Station upStation = new Station(rs.getLong("up_station_id"), rs.getString("upName"));
        final Station downStation = new Station(rs.getLong("down_station_id"), rs.getString("downName"));

        return new Path(rs.getLong("id"), upStation, downStation, rs.getInt("distance"));
    };

    public Paths findByLineId(final Long lineId) {
        final String sql = "SELECT p.id, up_station_id, s1.name AS upName, down_station_id, s2.name AS downName, distance\n" +
                "FROM PATH p\n" +
                "         JOIN station s1 ON p.up_station_id = s1.id\n" +
                "         JOIN station s2 ON p.down_station_id = s2.id\n" +
                "WHERE p.line_id = ?;";

        final List<Path> paths = jdbcTemplate.query(sql, rowMapper, lineId);
        return new Paths(paths);
    }

    public void save(final Paths paths, final Long lineId) {
        final String deleteLineSql = "DELETE FROM path WHERE line_id = ?";
        jdbcTemplate.update(deleteLineSql, lineId);

        final List<Path> pathList = paths.toList();
        final String sql = "INSERT INTO path (line_id, up_station_id, distance, down_station_id) VALUES (?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
                pathList,
                pathList.size(),
                (ps, argument) -> {
                    ps.setLong(1, lineId);
                    ps.setLong(2, argument.getUp().getId());
                    ps.setInt(3, argument.getDistance());
                    ps.setLong(4, argument.getDown().getId());
                });
    }
}
