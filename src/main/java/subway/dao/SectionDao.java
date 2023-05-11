package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<List<Long>> sectionIdRowMapper = (rs, rowNum) ->
            new ArrayList<>(
                    List.of(rs.getLong("from_id"),
                            rs.getLong("to_id"))
            );

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final Long fromId, final Long toId, final Integer distance, final Long lineId) {
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("from_id", fromId)
                .addValue("to_id", toId)
                .addValue("distance", distance)
                .addValue("line_id", lineId);

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Integer count(final Long lineId, final Long stationId) {
        final String sql = "SELECT COUNT (*) FROM section WHERE (section.from_id = ? OR section.to_id = ?) AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, stationId, stationId, lineId);
    }

    public List<List<Long>> findAdjacentStationId(final Long lineId, final Long stationId) {
        final String sql = "SELECT section.from_id, section.to_id FROM section WHERE (section.from_id =? OR section.to_id = ?)  AND section.line_id = ?";
        return jdbcTemplate.query(sql, sectionIdRowMapper, stationId, stationId, lineId);
    }

    public Long findLeftStationId(final Long lineId, final Long stationId) {
        final String sql = "SELECT section.from_id FROM section WHERE section.to_id = ?  AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, stationId, lineId);
    }

    public Long findRightStationId(final Long lineId, final Long stationId) {
        final String sql = "SELECT section.to_id FROM section WHERE section.from_id = ? AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, stationId, lineId);
    }

    public void deleteSection(final Long lineId, final Long stationId) {
        final String sql = "DELETE FROM section WHERE line_id = ? and (section.from_id =? OR section.to_id = ?)";
        jdbcTemplate.update(sql, lineId, stationId, stationId);
    }

    public Integer findLeftSectionDistance(final Long lineId, final Long stationId) {
        final String sql = "SELECT section.distance FROM section WHERE section.to_id = ? AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, stationId, lineId);
    }

    public Integer findRightSectionDistance(final Long lineId, final Long stationId) {
        final String sql = "SELECT section.distance FROM section WHERE section.from_id = ? AND section.line_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, stationId, lineId);
    }
}
