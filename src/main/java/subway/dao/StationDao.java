package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import java.sql.PreparedStatement;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long insert(StationEntity stationEntity) {
        String sql = "INSERT INTO STATION (name, next_station, distance, line_id) values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, stationEntity.getName());
            ps.setLong(2, stationEntity.getNextStationId());
            ps.setLong(3, stationEntity.getDistance());
            ps.setLong(4, stationEntity.getLineId());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Integer update(Long id, StationEntity stationEntity) {
        String sql = "UPDATE STATION SET name = ?, next_station = ?, distance = ? WHERE id = ?";
        return jdbcTemplate.update(sql, stationEntity.getName(), stationEntity.getNextStationId(), stationEntity.getDistance(), id);
    }
}
