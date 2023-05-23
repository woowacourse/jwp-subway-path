package subway.repository.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.repository.entity.SectionEntity;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private static final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("source_station_id"),
                    rs.getLong("target_station_id"),
                    rs.getLong("line_id"),
                    rs.getInt("distance")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(SectionEntity sectionEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("source_station_id", sectionEntity.getSourceStationId());
        params.put("target_station_id", sectionEntity.getTargetStationId());
        params.put("line_id", sectionEntity.getLineId());
        params.put("distance", sectionEntity.getDistance());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        String sql = "SELECT id, source_station_id, target_station_id, line_id, distance FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        String sql = "SELECT id, source_station_id, target_station_id, line_id, distance FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void deleteByLineId(Long lineId) {
        String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public boolean existsByStationId(Long stationId) {
        String sql = "select exists(select * from section where source_station_id = ? or target_station_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, stationId, stationId);
    }
}
