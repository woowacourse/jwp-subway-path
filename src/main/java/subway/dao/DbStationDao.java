package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DbStationDao implements StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertStation;

    public DbStationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertStation = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Station saveStation(Station station) {
        Map<String, Object> parameters = new HashMap<>(3);
        parameters.put("name", station.getName());
        final long newId = insertStation.executeAndReturnKey(parameters).longValue();
        return new Station(newId, station.getName());
    }

    @Override
    public List<Number> saveStations(final List<Station> stations) {
        return null;
    }

//    public Long delete(Long stationId) {
//        String sql = "delete from station where id = ?";
//        jdbcTemplate.update(sql,stationId);
//    }
}

