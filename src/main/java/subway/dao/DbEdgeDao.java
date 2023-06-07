package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Station;
import subway.entity.EdgeEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class DbEdgeDao implements EdgeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<EdgeEntity> rowMapper = (rs, rowNum) ->
            new EdgeEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("station_id"),
                    rs.getInt("station_order"),
                    rs.getInt("distance_to_next")
            );

    public DbEdgeDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("edge")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Long save(EdgeEntity edgeEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", edgeEntity.getLineId());
        params.put("station_id", edgeEntity.getStationId());
        params.put("station_order", edgeEntity.getStationOrder());
        params.put("distance_to_next", edgeEntity.getDistanceToNext());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    @Override
    public void deleteAllEdgesOf(Long lineId) {
        String sql = "delete from edge where line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
