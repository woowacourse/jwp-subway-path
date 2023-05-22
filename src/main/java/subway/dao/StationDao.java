package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

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
            if (stationEntity.getNextStationId() != null) {
                ps.setLong(2, stationEntity.getNextStationId());
            } else {
                ps.setNull(2, Types.BIGINT);
            }
            if (stationEntity.getDistance() != null) {
                ps.setInt(3, stationEntity.getDistance());
            } else {
                ps.setNull(3, Types.INTEGER);
            }
            if (stationEntity.getDistance() != null) {
                ps.setLong(4, stationEntity.getLineId());
            } else {
                ps.setNull(4, Types.BIGINT);
            }
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public Long update(Long id, StationEntity stationEntity) {
        String sql = "UPDATE STATION SET name = ?, next_station = ?, distance = ? WHERE id = ?";
        return Long.valueOf(jdbcTemplate.update(sql, stationEntity.getName(), stationEntity.getNextStationId(), stationEntity.getDistance(), id));
    }

    public Optional<StationEntity> findByName(String name) {
        String sql = "SELECT * FROM STATION WHERE name = ?";

        List<StationEntity> result = jdbcTemplate.query(sql, getStationRowMapper(), name);

        if (result.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(result.get(0));
        }
    }

    public Long remove(Long id) {
        String query = "DELETE FROM STATION WHERE id = ?";
        return Long.valueOf(jdbcTemplate.update(query, id));
    }

    public void removeAll() {
        String query = "DELETE FROM STATION";
        jdbcTemplate.update(query);
    }

    public List<StationEntity> findAll() {
        String sql = "SELECT * FROM STATION";

        return jdbcTemplate.query(sql, getStationRowMapper());
    }

    public Optional<StationEntity> findStationEntityById(Long id) {
        String sql = "SELECT * FROM STATION WHERE id = ?";
        List<StationEntity> stationEntities = jdbcTemplate.query(sql, getStationRowMapper(), id);

        if (stationEntities.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(stationEntities.get(0));
        }
    }

    private RowMapper<StationEntity> getStationRowMapper() {
        return (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");
            String stationName = resultSet.getString("name");
            Long nextStationId = resultSet.getLong("next_station");
            if (resultSet.wasNull()) {
                nextStationId = null; // NULL 값인 경우에는 변수에 null을 할당
            }
            Integer distance = resultSet.getInt("distance");
            if (resultSet.wasNull()) {
                distance = null; // NULL 값인 경우에는 변수에 null을 할당
            }
            Long lineId = resultSet.getLong("line_id");
            if (resultSet.wasNull()) {
                lineId = null; // NULL 값인 경우에는 변수에 null을 할당
            }

            return new StationEntity(id, stationName, nextStationId, distance, lineId);
        };
    }
}
