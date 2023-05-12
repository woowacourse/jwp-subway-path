package subway.dao;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("section")
            .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(final SectionEntity sectionEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("left_station_id", sectionEntity.getLeftStationId());
        params.put("right_station_id", sectionEntity.getRightStationId());
        params.put("distance", sectionEntity.getDistance());

        Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(sectionId, sectionEntity.getLeftStationId(), sectionEntity.getRightStationId(), sectionEntity.getDistance());
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM SECTION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
