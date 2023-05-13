package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<StationEntity> stationEntityRowMapper =
            (rs, rowNum) -> new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("line_id"));

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public StationEntity insert(StationEntity stationEntity) {
        String sql = "INSERT INTO station (name, line_id) VALUES (?, ?)";
        String stationName = stationEntity.getStationName();
        Long lineId = stationEntity.getLineId();

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setString(1, stationName);
            preparedStatement.setLong(2, lineId);
            return preparedStatement;
        }, keyHolder);

        long insertedId = keyHolder.getKey().longValue();
        return new StationEntity(insertedId, stationName, lineId);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "select id, name, line_id from STATION where id = ?";
        List<StationEntity> findStationEntity = jdbcTemplate.query(sql, stationEntityRowMapper, id);
        return findStationEntity.stream().findAny();
    }

    public Optional<StationEntity> findByStationIdAndLineId(Long stationId, Long lineId) {
        String sql = "SELECT id, name, line_id FROM STATION " +
                "WHERE id = ? AND line_id = ?";

        List<StationEntity> station = jdbcTemplate.query(sql, stationEntityRowMapper, stationId, lineId);
        return station.stream().findAny();
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
