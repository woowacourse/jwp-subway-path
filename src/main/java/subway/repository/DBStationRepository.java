package subway.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.StationDao;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.domain.station.StationRepository;
import subway.entity.StationEntity;

import java.util.List;

@Repository
public class DBStationRepository implements StationRepository {

    private final JdbcTemplate jdbcTemplate;
    private final StationDao stationDao;
    private final RowMapper<Station> stationRowMapper =
            (rs, rowNum) -> new Station(
                    rs.getLong("station_id"),
                    rs.getString("station_name"),
                    new Line(rs.getLong("line_id"), rs.getString("line_name"), rs.getInt("line_surcharge"))
            );

    public DBStationRepository(JdbcTemplate jdbcTemplate, StationDao stationDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.stationDao = stationDao;
    }

    @Override
    public Station insert(Station station) {
        StationEntity insertedEntity = stationDao.insert(new StationEntity(null, station.getName(), station.getLineId()));
        return new Station(insertedEntity.getId(), station.getName(), station.getLine());
    }

    @Override
    public Station findStationById(Long stationId) {
        String sql = "SELECT " +
                "station.id AS station_id, " +
                "station.name AS station_name, " +
                "line.id AS line_id, " +
                "line.name AS line_name, " +
                "line.surcharge AS line_surcharge " +
                "FROM STATION " +
                "INNER JOIN line ON station.line_id = line.id " +
                "WHERE station.id = ?";
        return jdbcTemplate.queryForObject(sql, stationRowMapper, stationId);
    }

    @Override
    public List<Station> findStationsByLineId(Long lineId) {
        String sql = "SELECT " +
                "station.id AS station_id, " +
                "station.name AS station_name, " +
                "line.id AS line_id, " +
                "line.name AS line_name, " +
                "line.surcharge AS line_surcharge " +
                "FROM STATION " +
                "INNER JOIN line ON station.line_id = line.id " +
                "WHERE line.id = ?";
        return jdbcTemplate.query(sql, stationRowMapper, lineId);
    }

    @Override
    public void remove(Station stationToDelete) {
        stationDao.deleteById(stationToDelete.getId());
    }
}
