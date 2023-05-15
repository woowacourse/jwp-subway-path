package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) -> new SectionEntity(
        rs.getLong("id"),
        rs.getLong("left_station_id"),
        rs.getLong("right_station_id"),
        rs.getLong("line_id"),
        rs.getInt("distance")
    );

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("section")
            .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(final SectionEntity section) {
        Map<String, Object> params = new HashMap<>();
        params.put("left_station_id", section.getLeftStationId());
        params.put("right_station_id", section.getRightStationId());
        params.put("distance", section.getDistance());
        params.put("line_id", section.getLineId());

        long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(
                sectionId,
                section.getLeftStationId(),
                section.getRightStationId(),
                section.getLineId(), section.
                getDistance());
    }

    public List<SectionEntity> findByLineId(final Long lindId) {
        String sql = "SELECT id, left_station_id, right_station_id, line_id, distance FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lindId);
    }
}
