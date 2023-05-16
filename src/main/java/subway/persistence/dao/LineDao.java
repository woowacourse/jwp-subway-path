package subway.persistence.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Line;
import subway.domain.Sections;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<Line> lineRowMapper = (rs, rowNum) ->
        new Line(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            new Sections(new ArrayList<>())
        );

    private final RowMapper<LineSectionStationJoinDto> lineSectionStationJoinRowMapper = (rs, rowNum) ->
        new LineSectionStationJoinDto(
            rs.getLong("line_id"),
            rs.getString("line_name"),
            rs.getString("line_color"),
            rs.getLong("start_station_id"),
            rs.getString("start_station_name"),
            rs.getLong("end_station_id"),
            rs.getString("end_station_name"),
            rs.getLong("section_id"),
            rs.getLong("section_distance")
        );

    public LineDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("line")
            .usingGeneratedKeyColumns("id");
    }

    public Line insert(final Line line) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", line.getName());
        params.put("color", line.getColor());
        final Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor());
    }

    public List<LineSectionStationJoinDto> findAll() {
        final String sql = "SELECT line.id                 AS line_id, "
            + "       line.name               AS line_name, "
            + "       line.color              AS line_color, "
            + "       start_station.id        AS start_station_id, "
            + "       start_station.name      AS start_station_name, "
            + "       end_station.id          AS end_station_id, "
            + "       end_station.name        AS end_station_name, "
            + "       section.id              AS section_id, "
            + "       section.line_id         AS section_line_id, "
            + "       section.up_station_id   AS section_up_station_id, "
            + "       section.down_station_id AS section_down_station_id, "
            + "       section.distance        AS section_distance "
            + "FROM SECTION AS section "
            + "         JOIN LINE AS line ON line.id = section.line_id "
            + "         JOIN STATION AS start_station ON start_station.id = section.up_station_id "
            + "         JOIN STATION AS end_station ON end_station.id = section.down_station_id ";
        return jdbcTemplate.query(sql, lineSectionStationJoinRowMapper);
    }

    public List<LineSectionStationJoinDto> findById(final Long id) {
        final String sql = "SELECT line.id                 AS line_id, "
            + "       line.name               AS line_name, "
            + "       line.color              AS line_color, "
            + "       start_station.id        AS start_station_id, "
            + "       start_station.name      AS start_station_name, "
            + "       end_station.id          AS end_station_id, "
            + "       end_station.name        AS end_station_name, "
            + "       section.id              AS section_id, "
            + "       section.line_id         AS section_line_id, "
            + "       section.up_station_id   AS section_up_station_id, "
            + "       section.down_station_id AS section_down_station_id, "
            + "       section.distance        AS section_distance "
            + "FROM SECTION AS section "
            + "         JOIN LINE AS line ON line.id = section.line_id "
            + "         JOIN STATION AS start_station ON start_station.id = section.up_station_id "
            + "         JOIN STATION AS end_station ON end_station.id = section.down_station_id "
            + "WHERE line.id = ?;";
        return jdbcTemplate.query(sql, lineSectionStationJoinRowMapper, id);
    }

    public void update(final Line newLine) {
        final String sql = "update LINE set name = ?, color = ? where id = ?";
        jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), newLine.getId());
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }

    public List<LineSectionStationJoinDto> findByName(final String name) {
        final String sql = "SELECT line.id                 AS line_id, "
            + "       line.name               AS line_name, "
            + "       line.color              AS line_color, "
            + "       start_station.id        AS start_station_id, "
            + "       start_station.name      AS start_station_name, "
            + "       end_station.id          AS end_station_id, "
            + "       end_station.name        AS end_station_name, "
            + "       section.id              AS section_id, "
            + "       section.line_id         AS section_line_id, "
            + "       section.up_station_id   AS section_up_station_id, "
            + "       section.down_station_id AS section_down_station_id, "
            + "       section.distance        AS section_distance "
            + "FROM SECTION AS section "
            + "         JOIN LINE AS line ON line.id = section.line_id "
            + "         JOIN STATION AS start_station ON start_station.id = section.up_station_id "
            + "         JOIN STATION AS end_station ON end_station.id = section.down_station_id "
            + "WHERE line.name = ?;";
        return jdbcTemplate.query(sql, lineSectionStationJoinRowMapper, name);
    }

    public void delete(final Line line) {
        final String sql = "DELETE "
            + "FROM LINE "
            + "WHERE id = ?";
        jdbcTemplate.update(sql, line.getId());
    }

    public Line findOnlyLineById(final Long id) {
        final String sql = "SELECT id, name, color "
            + "FROM LINE "
            + "WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, lineRowMapper, id);
    }

    public Line findOnlyLineByName(final String name) {
        final String sql = "SELECT id, name, color "
            + "FROM LINE "
            + "WHERE name = ?;";
        return jdbcTemplate.queryForObject(sql, lineRowMapper, name);
    }

    public List<Line> findOnlyLines() {
        final String sql = "SELECT id, name, color "
            + "FROM LINE;";
        return jdbcTemplate.query(sql, lineRowMapper);
    }
}
