package subway.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.domain.station.Station;

@Repository
public class StationDao {

    private static final String STATION_LINE_JOIN_SQL =
            "SELECT station.id AS station_id, station.name AS station_name, line.id AS line_id, line.name AS line_name " +
            "FROM STATION " +
            "INNER JOIN LINE " +
            "ON STATION.line_id = LINE.id ";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Station> rowMapper = (rs, rowNum) -> {
        long findStationId = rs.getLong("station_id");
        String findStationName = rs.getString("station_name");
        long findLineId = rs.getLong("line_id");
        String findLineName = rs.getString("line_name");

        return new Station(findStationId, findStationName, new Line(findLineId, findLineName));
    };

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station").usingGeneratedKeyColumns("id");
    }

    public Station insert(Station station) {
        String name = station.getName();
        Line line = station.getLine();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("line_id", line.getId());

        long insertedId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Station(insertedId, name, line);
    }

    public Optional<Station> selectById(Long id) {
        String sql = STATION_LINE_JOIN_SQL + "WHERE STATION.id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findAny();
    }

    public Optional<Station> selectByStationNameAndLineName(String stationName, String lineName) {
        String sql = STATION_LINE_JOIN_SQL + "WHERE station.name = ? AND line.name = ?";

        return jdbcTemplate.query(sql, rowMapper, stationName, lineName).stream().findAny();
    }

    public List<Station> selectAllByLineId(Long lineId) {
        String sql = STATION_LINE_JOIN_SQL + "WHERE line.id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<Station> selectAll() {
        String sql = STATION_LINE_JOIN_SQL;

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Boolean isNotExistById(Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM STATION WHERE id = ?)";
        return Boolean.FALSE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, id));
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
