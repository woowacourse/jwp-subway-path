package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.InterStationEntity;

@Repository
public class InterStationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public InterStationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("INTERSTATION")
                .usingGeneratedKeyColumns("id");
    }

    public void deleteAllByLineId(final long lineId) {
        final String sql = "delete from INTERSTATION where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void insertAll(final List<InterStationEntity> interStationEntities) {
        @SuppressWarnings("unchecked") final Map<String, Object>[] parameters = interStationEntities.stream()
                .map(interStationEntity -> {
                    final Map<String, Object> parameter = new HashMap<>();
                    parameter.put("line_id", interStationEntity.getLineId());
                    parameter.put("start_station_id", interStationEntity.getFrontStationId());
                    parameter.put("end_station_id", interStationEntity.getBackStationId());
                    parameter.put("distance", interStationEntity.getDistance());
                    return parameter;
                }).toArray(Map[]::new);
        simpleJdbcInsert.executeBatch(parameters);
    }

    public List<InterStationEntity> findAll() {
        final String sql = "select id,line_id,start_station_id,end_station_id,distance from INTERSTATION";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            final long id = rs.getLong("id");
            final long lineId = rs.getLong("line_id");
            final long startStationId = rs.getLong("start_station_id");
            final long endStationId = rs.getLong("end_station_id");
            final int distance = rs.getInt("distance");
            return new InterStationEntity(id, lineId, startStationId, endStationId, distance);
        });
    }
}
