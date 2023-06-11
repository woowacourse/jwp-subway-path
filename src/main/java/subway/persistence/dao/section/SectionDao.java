package subway.persistence.dao.section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.section.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("section_id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getLong("distance")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public void batchSave(final List<SectionEntity> sectionEntities) {
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

    public void deleteAllByLineId(final Long lineId) {
        String sql = "DELETE FROM section WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public List<SectionEntity> findAllByLineId(final Long lineId) {
        String sql = "SELECT section_id, line_id, up_station_id, down_station_id, distance FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<SectionEntity> findAllByLineIds(final List<Long> lineIds) {
        if (lineIds.isEmpty()) {
            return Collections.emptyList();
        }
        String sql = "SELECT * FROM section WHERE id IN (:lineIds)";
        MapSqlParameterSource source = new MapSqlParameterSource();
        source.addValue("lineIds", lineIds);
        return namedParameterJdbcTemplate.query(sql, source, rowMapper);
    }
}