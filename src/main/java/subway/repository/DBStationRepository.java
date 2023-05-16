package subway.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class DBStationRepository implements StationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Station> stationRowMapper =
            (rs, rowNum) -> new Station(
                    rs.getLong("station_id"),
                    rs.getString("station_name"),
                    new Line(rs.getLong("line_id"), rs.getString("line_name"))
            );

    public DBStationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Station> findStationById(Long stationId) {
        String sql = "SELECT " +
                "station.id AS station_id, " +
                "station.name AS station_name, " +
                "line.id AS line_id, " +
                "line.name AS line_name " +
                "FROM STATION " +
                "INNER JOIN line ON station.line_id = line.id " +
                "WHERE station_id = ?";
        List<Station> findStation = jdbcTemplate.query(sql, stationRowMapper, stationId);
        return findStation.stream().findAny();
    }
}
