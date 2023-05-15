package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineStationEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class LineStationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private RowMapper<LineStationEntity> rowMapper = ((rs, rowNum) ->
            new LineStationEntity(
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

    public LineStationEntity insert(final LineStationEntity lineStationEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("station_id", lineStationEntity.getStationId());
        params.put("line_id", lineStationEntity.getLineId());

        Long lineStationId = insertAction.executeAndReturnKey(params).longValue();
        return new LineStationEntity(lineStationId, lineStationEntity.getStationId(), lineStationEntity.getLineId());
    }

    public void deleteByLineId(final Long lineId) {
        String sql = "DELETE FROM LINE_STATION WHERE Line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        String sql = "DELETE FROM LINE_STATION WHERE Line_id = ? AND station_id = ?";
        jdbcTemplate.update(sql, lineId, stationId);
    }
}
