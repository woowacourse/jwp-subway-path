package subway.persistence.dao;

import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("section")
            .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(final SectionEntity sectionEntity, final long lindId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("distance", sectionEntity.getDistance());
        params.put("line_id", lindId);
        params.put("up_station_id", sectionEntity.getUpStationId());
        params.put("down_station_id", sectionEntity.getDownStationId());
        final Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(sectionId, sectionEntity.getUpStationId(),
            sectionEntity.getDownStationId(), sectionEntity.getDistance());
    }

    public void deleteAllByLineId(final Long lineId) {
        final String sql = "DELETE "
            + "FROM SECTION "
            + "WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public boolean containId(final Long id) {
        final String sql = "SELECT count(id) "
            + "FROM SECTION "
            + "WHERE line_id = ?";
        final Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != 0;
    }
}
