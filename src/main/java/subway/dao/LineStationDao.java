package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineStationEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineStationDao {

    private static final RowMapper<LineStationEntity> rowMapper = (rs, rows) -> new LineStationEntity(rs.getLong("id"), rs.getLong("station_id"), rs.getLong("line_id"));
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

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

    public List<LineStationEntity> findByStationId(Long stationId) {
        String sql = "SELECT id, station_id, line_id FROM LINE_STATION WHERE station_id = ?";
        return jdbcTemplate.query(sql, rowMapper, stationId);
    }

    public List<LineStationEntity> findByLineId(Long lineId) {
        String sql = "SELECT id, station_id, line_id FROM LINE_STATION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }
}
