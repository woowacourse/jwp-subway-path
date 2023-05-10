package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineStation;

@Repository
public class LineStationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private RowMapper<LineStation> rowMapper = ((rs, rowNum) ->
        new LineStation(
            rs.getLong("id"),
            rs.getLong("station_id"),
            rs.getLong("line_id")
        ));


    public LineStationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("line_station")
            .usingGeneratedKeyColumns("id");
    }

    public LineStation insert(final LineStation lineStation) {
        Map<String, Object> params = new HashMap<>();
        params.put("station_id", lineStation.getStationId());
        params.put("line_id", lineStation.getLineId());

        Long lineStationId = insertAction.executeAndReturnKey(params).longValue();
        return new LineStation(lineStationId, lineStation.getStationId(), lineStation.getLineId());
    }

    public List<LineStation> findByLineId(final Long lineId) {
        String sql = "SELECT id, station_id, line_id from LINE_STATION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }
}
