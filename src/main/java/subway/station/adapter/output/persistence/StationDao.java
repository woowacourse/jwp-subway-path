package subway.station.adapter.output.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.station.domain.Station;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );
    
    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }
    
    public Long insert(final StationEntity entity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", entity.getName());
        
        return insertAction.executeAndReturnKey(params).longValue();
    }
    
    public List<StationEntity> findAll() {
        final String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, rowMapper);
    }
    
    public StationEntity findById(final Long id) {
        final String sql = "select * from station WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }
    
    public StationEntity findByName(final Station name) {
        final String sql = "select * from station WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name.getName());
    }
}
