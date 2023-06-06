package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.subwaymap.Line;
import subway.dto.LineSectionDto;

@Component
public class LineDao {

    private static final RowMapper<Line> LINE_ROW_MAPPER = (rs, rowNum) ->
        Line.of(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            rs.getInt("additional_fare")
        );
    private static final RowMapper<LineSectionDto> LINE_SECTION_DTO_ROW_MAPPER = (rs, rowNum) ->
        new LineSectionDto(
            rs.getLong("line_id"),
            rs.getString("line_name"),
            rs.getString("line_color"),
            rs.getInt("line_additional_fare"),
            rs.getLong("sec_id"),
            rs.getInt("distance"),
            rs.getLong("up_station_id"),
            rs.getString("up_station_name"),
            rs.getLong("down_station_id"),
            rs.getString("down_station_name")
        );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public LineDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("LINE")
            .usingGeneratedKeyColumns("id");
    }

    public Line insert(final Line line) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("additional_fare", line.getAdditionalFare());

        final Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return Line.of(lineId, line.getName(), line.getColor(), line.getAdditionalFare());
    }

    public Optional<Line> findById(Long id) {
        final String sql = "SELECT * FROM LINE WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, LINE_ROW_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Line> findAll() {
        final String sql = "SELECT * FROM LINE";
        return jdbcTemplate.query(sql, LINE_ROW_MAPPER);
    }

    public List<LineSectionDto> findAllSections() {
        final String sql = "SELECT "
            + "LINE.id AS line_id, LINE.name AS line_name, "
            + "LINE.color AS line_color, LINE.additional_fare AS line_additional_fare, "
            + "SEC.id AS sec_id, SEC.distance AS distance, "
            + "S1.id as up_station_id, S1.name as up_station_name, "
            + "S2.id as down_station_id, S2.name as down_station_name "
            + "FROM LINE AS LINE "
            + "INNER JOIN LINE_SECTION AS SEC ON SEC.line_id = LINE.id "
            + "INNER JOIN STATION AS S1 ON S1.id = SEC.up_station_id "
            + "INNER JOIN STATION AS S2 ON S2.id = SEC.down_station_id ";
        return jdbcTemplate.query(sql, LINE_SECTION_DTO_ROW_MAPPER);
    }

    public void update(final Line newLine) {
        final String sql = "UPDATE LINE SET name = ?, color = ?, additional_fare = ? WHERE id = ?";
        jdbcTemplate.update(sql, newLine.getName(), newLine.getColor(), newLine.getId(), newLine.getAdditionalFare());
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM LINE WHERE id = ?", id);
    }
}
