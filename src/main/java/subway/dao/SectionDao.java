package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.subwaymap.Section;
import subway.domain.subwaymap.Station;

@Component
public class SectionDao {

    private static final RowMapper<Section> SECTION_ROW_MAPPER = (rs, rowNum) ->
        Section.of(
            rs.getLong("line_id"),
            Station.of(rs.getLong("up_station_id"), rs.getString("up_station_name")),
            Station.of(rs.getLong("down_station_id"), rs.getString("down_station_name")),
            rs.getInt("distance")
        );
    private static final RowMapper<Long> LONG_ROW_MAPPER = (rs, rowNum) -> (Long) rs.getLong("id");

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("LINE_SECTION")
            .usingGeneratedKeyColumns("id");
    }

    public Section insert(final Long lineId, final Section section) {
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("distance", section.getDistance());
        params.addValue("up_station_id", section.getUpStation().getId());
        params.addValue("down_station_id", section.getDownStation().getId());
        params.addValue("line_id", lineId);
        final long sectionId = simpleJdbcInsert.executeAndReturnKey(params)
            .longValue();
        return Section.of(sectionId, section.getUpStation(), section.getDownStation(), section.getDistance());
    }

    public Long findIdBy(final Long lineId, final Section section) {
        final String sql = "SELECT id FROM LINE_SECTION WHERE up_station_id = ? AND down_station_id = ? AND line_id = ?";
        final Long upStationId = section.getUpStation().getId();
        final Long downStationId = section.getDownStation().getId();
        return jdbcTemplate.queryForObject(sql, LONG_ROW_MAPPER, upStationId, downStationId, lineId);
    }

    public List<Section> findAllByLineId(final Long id) {
        final String sql = "SELECT "
            + "SEC.id AS line_id, SEC.distance AS distance, "
            + "S1.id as up_station_id, S1.name as up_station_name, "
            + "S2.id as down_station_id, S2.name as down_station_name "
            + "FROM LINE_SECTION AS SEC "
            + "INNER JOIN STATION AS S1 ON S1.id = SEC.up_station_id "
            + "INNER JOIN STATION AS S2 ON S2.id = SEC.down_station_id "
            + "WHERE line_id = ?";

        return jdbcTemplate.query(sql, SECTION_ROW_MAPPER, id);
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE FROM LINE_SECTION WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }
}
