package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<StationEntity> stationEntityRowMapper =
            (rs, rowNum) -> new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getLong("line_id"));

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(StationEntity stationEntity) {
        String stationName = stationEntity.getStationName();
        Long lineId = stationEntity.getLineId();

        Map<String, Object> params = new HashMap<>();
        params.put("name", stationName);
        params.put("line_id", lineId);

        long insertedId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new StationEntity(insertedId, stationName, lineId);
    }

    public Optional<StationEntity> findByStationNameAndLineId(String stationName, Long lineId) {
        String sql = "SELECT id, name, line_id FROM STATION " +
                "WHERE name = ? AND line_id = ?";

        List<StationEntity> station = jdbcTemplate.query(sql, stationEntityRowMapper, stationName, lineId);
        return station.stream().findAny();
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
