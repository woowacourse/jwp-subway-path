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
public class DbEdgeDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<EdgeEntity> rowMapper = (rs, rowNum) ->
            new EdgeEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("station_id"),
                    rs.getLong("station_order")
            );

    public DbEdgeDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("edge")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(Line line, Station station, Long stationOrder) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", line.getId());
        params.put("station_id", station.getId());
        params.put("station_order", stationOrder);

        return insertAction.executeAndReturnKey(params).longValue();
    }
}
