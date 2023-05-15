package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.SectionEntity;
import subway.entity.SectionStationEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final RowMapper<SectionStationEntity> sectionStationrowMapper = (rs, rowNum) -> new SectionStationEntity(
            rs.getLong("id"),
            rs.getLong("left_station_id"),
            rs.getNString(3),
            rs.getLong("right_station_id"),
            rs.getNString(5),
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
                section.getLineId(),
                section.getDistance());
    }

    public List<SectionStationEntity> findByLineId(final Long lindId) {
        String sql = "SELECT s.id, s.left_station_id, t1.name, s.right_station_id, t2.name, s.distance FROM SECTION s " +
                "JOIN station t1 ON s.left_station_id = t1.id " +
                "JOIN station t2 ON s.right_station_id = t2.id " +
                "WHERE s.line_id= ? ";
        return jdbcTemplate.query(sql, sectionStationrowMapper, lindId);
    }
}
