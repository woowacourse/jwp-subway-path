package subway.dao.section;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("section_id");
    }

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("section_id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getLong("distance")
            );

    public Long insert(final SectionEntity sectionEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", sectionEntity.getLineId());
        params.put("up_station_id", sectionEntity.getUpStationId());
        params.put("down_station_id", sectionEntity.getDownStationId());
        params.put("distance", sectionEntity.getDistance());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<SectionEntity> findSectionsByLineId(final Long lineId) {
        String sql = "SELECT section_id, line_id, up_station_id, down_station_id, distance FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void delete(final SectionEntity sectionEntity) {
        String sql = "DELETE FROM section WHERE line_id = ? AND up_station_id = ? AND down_station_id = ?";
        jdbcTemplate.update(sql, sectionEntity.getLineId(), sectionEntity.getUpStationId(), sectionEntity.getDownStationId());
    }

    public void deleteAllByLineId(final Long lineId) {
        String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public void insertBatchSections(final List<SectionEntity> sectionEntities) {
        String sql = "INSERT INTO section (line_id, up_station_id, down_station_id, distance) VALUES (?, ?, ?, ?)";
        List<Object[]> batchValues = new ArrayList<>();

        for (SectionEntity entity : sectionEntities) {
            Object[] values = new Object[]{
                    entity.getLineId(),
                    entity.getUpStationId(),
                    entity.getDownStationId(),
                    entity.getDistance()
            };
            batchValues.add(values);
        }

        jdbcTemplate.batchUpdate(sql, batchValues);
    }
}
