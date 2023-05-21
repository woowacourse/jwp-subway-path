package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;

@Repository
public class SectionDao {
    private static final RowMapper<SectionEntity> ENTITY_MAPPER = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("source_station_id"),
                    rs.getLong("target_station_id"),
                    rs.getInt("distance"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT id, line_id, source_station_id, target_station_id, distance "
                + "FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, ENTITY_MAPPER, lineId);
    }

    public void deleteByLineId(final Long lineId) {
        final String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void insertAll(final List<SectionEntity> sectionEntities) {
        final String sql = "INSERT INTO section (source_station_id, target_station_id, line_id, distance) values (?, ?, ?, ?)";
        jdbcTemplate.batchUpdate(sql, sectionEntities, sectionEntities.size(), ((ps, section) -> {
            ps.setLong(1, section.getSourceStationId());
            ps.setLong(2, section.getTargetStationId());
            ps.setLong(3, section.getLineId());
            ps.setInt(4, section.getDistance());
        }));
    }

    public SectionEntity insert(final Long lineId, final Long sourceStationId, final Long targetStationId, final Integer distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("line_id", lineId);
        params.put("source_station_id", sourceStationId);
        params.put("target_station_id", targetStationId);
        params.put("distance", distance);
        final Long id = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(id, lineId, sourceStationId, targetStationId, distance);
    }

    public List<SectionEntity> findAll(){
        final String sql = "SELECT id, line_id, source_station_id, target_station_id, distance FROM section";
        return jdbcTemplate.query(sql, ENTITY_MAPPER);
    }

}
