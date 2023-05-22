package subway.section.adapter.output.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("first_station_id"),
                    rs.getLong("second_station_id"),
                    rs.getLong("distance"),
                    rs.getLong("line_id")
            );
    
    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }
    
    public Long insert(final SectionEntity entity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("first_station_id", entity.getFirstStationId());
        params.put("second_station_id", entity.getSecondStationId());
        params.put("distance", entity.getDistance());
        params.put("line_id", entity.getLineId());
        
        return insertAction.executeAndReturnKey(params).longValue();
    }
    
    public List<SectionEntity> findAll() {
        final String sql = "SELECT * FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }
    
    public void deleteByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
