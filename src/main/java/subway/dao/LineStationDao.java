package subway.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.domain.station.Station;
import subway.entity.LineStation;

@Repository
public class LineStationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;


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
}
