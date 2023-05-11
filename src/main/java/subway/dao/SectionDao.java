package subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT id, line_id, source_station_id, target_station_id, distance "
            + "FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, (result, count) ->
            new SectionEntity(
                result.getLong("id"),
                result.getLong("line_id"),
                result.getLong("source_station_id"),
                result.getLong("target_station_id"),
                result.getInt("distance")), lineId);
    }

    public long insert(final SectionEntity sectionEntity) {
        final String sql = "INSERT INTO section "
            + "(line_id, source_station_id, target_station_id, distance) "
            + "VALUES(?, ?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, sectionEntity.getLineId());
            ps.setLong(2, sectionEntity.getSourceStationId());
            ps.setLong(3, sectionEntity.getTargetStationId());
            ps.setInt(4, sectionEntity.getDistance());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public int deleteByLineIdAndSourceStationId(final Long lineId, final Long sourceStationId) {
        final String sql = "DELETE FROM section WHERE line_id = ? and source_station_id = ?";
        return jdbcTemplate.update(sql, lineId, sourceStationId);
    }

    public int deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        final String sql = "DELETE FROM section WHERE (line_id = ?  and source_station_id = ?) "
            + "OR (line_id = ?  and target_station_id = ?)";
        return jdbcTemplate.update(sql, lineId, stationId, lineId, stationId);
    }
}
