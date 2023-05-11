package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.StationLine;

import java.util.List;

@Repository
public class StationLineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public StationLineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("stations_lines")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<StationLine> joinLineRowMapper = (rs, rowNum) -> new StationLine(
            null,
            new Line(
                    rs.getLong("line_id"),
                    rs.getString("line_name"),
                    rs.getString("line_color")
            )
    );

    public Long save(final Long stationId, final Long lineId) {
        return simpleJdbcInsert.executeAndReturnKey(new MapSqlParameterSource()
                .addValue("station_id", stationId)
                .addValue("line_id", lineId)
        ).longValue();
    }

    public List<StationLine> findByStationName(final String stationName) {
        final String sql = "SELECT sl.id as station_line_id, l.id as line_id , l.name as line_name, l.color as line_color" +
                " FROM stations_lines sl" +
                " JOIN lines l ON sl.line_id = l.id" +
                " JOIN stations s ON s.id = sl.station_id" +
                " WHERE s.name = ?";

        return jdbcTemplate.query(sql, joinLineRowMapper, stationName);
    }

    public List<StationLine> findByStationId(final Long stationId) {
        final String sql = "SELECT sl.id as station_line_id, l.id as line_id , l.name as line_name, l.color as line_color" +
                " FROM stations_lines sl" +
                " JOIN lines l ON sl.line_id = l.id" +
                " JOIN stations s ON s.id = sl.station_id" +
                " WHERE s.id = ?";
        return jdbcTemplate.query(sql, joinLineRowMapper, stationId);
    }

    public List<StationLine> findByLineId(final Long lineId) {
        final String sql = "SELECT sl.id as station_line_id, l.id as line_id , l.name as line_name, l.color as line_color" +
                " FROM stations_lines sl" +
                " JOIN lines l ON sl.line_id = l.id" +
                " JOIN stations s ON s.id = sl.station_id" +
                " WHERE l.id = ?";
        return jdbcTemplate.query(sql, joinLineRowMapper, lineId);
    }

    public void deleteByStationIdAndLineId(final Long stationId, final Long lineId) {
        final String sql = "DELETE FROM stations_lines WHERE station_id = ? AND line_id = ?";
        jdbcTemplate.update(sql, stationId, lineId);
    }
}
