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
        final String sql = "SELECT id, line_id, source_station_id, target_station_id, distance, section_type "
            + "FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, (result, count) ->
            new SectionEntity(
                result.getLong("id"),
                result.getLong("line_id"),
                result.getLong("source_station_id"),
                result.getLong("target_station_id"),
                result.getInt("distance"),
                result.getString("section_type")), lineId);
    }

    public long insert(final SectionEntity sectionEntity) {
        final String sql = "INSERT INTO section "
            + "(line_id, source_station_id, target_station_id, distance, section_type) "
            + "VALUES(?, ?, ?, ?, ?)";
        final GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, sectionEntity.getLineId());
            ps.setLong(2, sectionEntity.getSourceStationId());
            ps.setLong(3, sectionEntity.getTargetStationId());
            ps.setInt(4, sectionEntity.getDistance());
            ps.setString(5, sectionEntity.getSectionType());
            return ps;
        }, keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).longValue();
    }

    public int updateSectionTypeByLineIdAndSourceStationId(final Long lineId, final Long sourceStationId,
                                                           final String sectionType) {
        final String sql = "UPDATE section SET section_type = ? WHERE line_id = ? and source_station_id = ?";
        return jdbcTemplate.update(sql, sectionType, lineId, sourceStationId);
    }

    public int deleteByLineIdAndSourceStationId(final Long lineId, final Long sourceStationId) {
        final String sql = "DELETE FROM section WHERE line_id = ? and source_station_id = ?";
        return jdbcTemplate.update(sql, lineId, sourceStationId);
    }
}
