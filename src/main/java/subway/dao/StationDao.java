package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.domain.station.Station;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private static final RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getString("name")
            );

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Station station) {
        String sql = "INSERT INTO STATION (name) VALUES (?)";
        jdbcTemplate.update(sql, station.getName());
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Station> findByName(String stationName) {
        String sql = "select * from STATION where name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationName));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(String prevStationName, Station station) {
        String sql = "update STATION set name = ? where name = ?";
        jdbcTemplate.update(sql, station.getName(), prevStationName);
    }

    public void deleteByName(String stationName) {
        String sql = "delete from STATION where name = ?";
        jdbcTemplate.update(sql, stationName);
    }

    public boolean existsBy(String stationName) {
        String sql = "SELECT COUNT(*) FROM STATION WHERE name = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, stationName) > 0;
    }

    public boolean doesNotExistBy(String stationName) {
        return !existsBy(stationName);
    }
}
