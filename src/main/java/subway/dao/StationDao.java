package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;
import subway.domain.Station;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(Station station) {
        String sql = "INSERT INTO STATION (name) VALUES (?)";
        jdbcTemplate.update(sql, station.getName());
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public StationEntity findByName(String stationName) {
        String sql = "select * from STATION where name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, stationName);
    }

    public StationEntity findById(Long id) {
        String sql = "SELECT * FROM STATION WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(String prevStationName, Station station) {
        String sql = "update STATION set name = ? where name = ?";
        jdbcTemplate.update(sql, prevStationName, station.getName());
    }

    public void deleteByName(String stationName) {
        String sql = "delete from STATION where name = ?";
        jdbcTemplate.update(sql, stationName);
    }

    public boolean existsBy(String stationName) {
        String sql = "SELECT COUNT(*) FROM STATION WHERE name = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, stationName) > 0;
    }
}
