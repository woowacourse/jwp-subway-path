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
import org.springframework.stereotype.Repository;
import subway.dao.dto.LineWithSection;
import subway.dao.entity.LineEntity;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<LineEntity> lineEntityMapper = (rs, rowNum) ->
        new LineEntity(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            rs.getInt("extra_fare")
        );

    private final RowMapper<LineWithSection> lineWithSectionMapper = (result, count) ->
        new LineWithSection(
            result.getLong("id"),
            result.getLong("line_id"),
            result.getString("line_name"),
            result.getString("line_color"),
            result.getInt("line_extra_fare"),
            result.getLong("source_station_id"),
            result.getString("source_station_name"),
            result.getLong("target_station_id"),
            result.getString("target_station_name"),
            result.getInt("distance")
        );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("line")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(final LineEntity lineEntity) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", lineEntity.getId());
        params.put("name", lineEntity.getName());
        params.put("color", lineEntity.getColor());
        params.put("extra_fare", lineEntity.getExtraFare());
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Optional<LineEntity> findById(final Long id) {
        final String sql = "SELECT id, name, color, extra_fare FROM line WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, lineEntityMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<LineWithSection> findByLindIdWithSections(final Long id) {
        final String sql = "SELECT sec.id, l.id as line_id, l.name as line_name, l.color as line_color, "
            + "l.extra_fare as line_extra_fare, "
            + "sec.source_station_id, sta1.name as source_station_name, "
            + "sec.target_station_id, sta2.name as target_station_name, sec.distance "
            + "FROM line l "
            + "LEFT JOIN section sec ON l.id = sec.line_id "
            + "LEFT JOIN station sta1 ON sec.source_station_id = sta1.id "
            + "LEFT JOIN station sta2 ON sec.target_station_id = sta2.id "
            + "WHERE l.id = ?";

        return jdbcTemplate.query(sql, lineWithSectionMapper, id);
    }

    public List<LineWithSection> findAllWithSections() {
        final String sql = "SELECT sec.id, l.id as line_id, l.name as line_name, l.color as line_color, "
            + "l.extra_fare as line_extra_fare, "
            + "sec.source_station_id, sta1.name as source_station_name, "
            + "sec.target_station_id, sta2.name as target_station_name, sec.distance "
            + "FROM line l "
            + "LEFT JOIN section sec ON l.id = sec.line_id "
            + "LEFT JOIN station sta1 ON sec.source_station_id = sta1.id "
            + "LEFT JOIN station sta2 ON sec.target_station_id = sta2.id ";

        return jdbcTemplate.query(sql, lineWithSectionMapper);
    }

    public int update(final LineEntity lineEntity) {
        final String sql = "UPDATE line SET name = ?, color = ? WHERE id = ?";
        return jdbcTemplate.update(sql, lineEntity.getName(), lineEntity.getColor(), lineEntity.getId());
    }

    public int deleteById(final Long id) {
        return jdbcTemplate.update("DELETE FROM line WHERE id = ?", id);
    }

    public boolean existByName(final String name) {
        final String sql = "SELECT COUNT(*) FROM line WHERE name = ?";
        final long count = jdbcTemplate.queryForObject(sql, Long.class, name);
        return count > 0;
    }

    public List<LineWithSection> getAllLineSectionsSourceAndTargetStationId(final Long sourceStationId,
                                                                            final Long targetStationId) {
        final String sql = "SELECT sec.id, l.id AS line_id, l.name AS line_name, l.color AS line_color, "
            + "l.extra_fare as line_extra_fare, "
            + "sec.source_station_id, sta1.name AS source_station_name, "
            + "sec.target_station_id, sta2.name AS target_station_name, sec.distance "
            + "FROM line l "
            + "INNER JOIN section sec ON l.id = sec.line_id "
            + "INNER JOIN station sta1 ON sec.source_station_id = sta1.id "
            + "INNER JOIN station sta2 ON sec.target_station_id = sta2.id "
            + "WHERE line_id IN ( "
            + "    SELECT DISTINCT s.line_id "
            + "    FROM section s "
            + "    WHERE (s.source_station_id = ? OR s.target_station_id = ?) "
            + "    OR (s.source_station_id = ? OR s.target_station_id = ?)"
            + ") "
            + "ORDER BY sec.id";

        return jdbcTemplate.query(sql, lineWithSectionMapper, sourceStationId, targetStationId, targetStationId,
            sourceStationId);
    }
}
