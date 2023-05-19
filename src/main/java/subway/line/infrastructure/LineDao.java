package subway.line.infrastructure;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.line.Line;
import subway.line.domain.station.Station;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LineDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line")
                .usingGeneratedKeyColumns("id");
    }

    public Line insert(String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, name, color);
    }

    public List<Line> findAll() {
        String sql = "select L.id as L_ID, L.name as L_NAME, L.color as L_COLOR, S.id as S_ID, S.name as S_NAME from LINE L " +
                "LEFT OUTER JOIN STATION S ON S.id = L.head_station_id";
        return jdbcTemplate.query(sql, (rs, rowNum) -> getLineFromRowSet(rs));
    }

    public Line findById(Long id) {
        String sql = "select L.id as L_ID, L.name as L_NAME, L.color as L_COLOR, S.id as S_ID, S.name as S_NAME from LINE L " +
                "LEFT OUTER JOIN STATION S ON S.id = L.head_station_id " +
                "WHERE L.id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> getLineFromRowSet(rs), id);
    }

    private static Line getLineFromRowSet(ResultSet rs) throws SQLException {
        Station headStation = null;
        if (rs.getLong("S_ID") != 0 && rs.getString("S_NAME") != null) {
            headStation = new Station(rs.getLong("S_ID"), rs.getString("S_NAME"));
        }
        return new Line(rs.getLong("L_ID"),
                rs.getString("L_NAME"),
                rs.getString("L_COLOR"),
                headStation);
    }

    public void update(Line newLine) {
        String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getName(), newLine.getColor(), newLine.getId()});
    }

    public void updateHeadStation(Line line, Station station) {
        final var sql = "update LINE set head_station_id = ? where id = ?";
        jdbcTemplate.update(sql, station.getId(), line.getId());
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
