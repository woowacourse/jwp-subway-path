package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.SectionEntity;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );

    public SectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(final SectionEntity sectionEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", sectionEntity.getId());
        params.put("line_id", sectionEntity.getLineId());
        params.put("up_station_id", sectionEntity.getUpStationId());
        params.put("down_station_id", sectionEntity.getDownStationId());
        params.put("distance", sectionEntity.getDistance());

        long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(sectionId, sectionEntity.getLineId(), sectionEntity.getUpStationId(), sectionEntity.getDownStationId(), sectionEntity.getDistance());
    }

    public List<SectionEntity> findAll() {
        String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<SectionEntity> findByLineId(final long lineId) {
        String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION where line_id = ?;";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public SectionEntity findById(final Long id) {
        String sql = "select id, line_id, up_station_id, down_station_id, distance from SECTION WHERE id = ?";

        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void deleteById(final Long id) {
        jdbcTemplate.update("delete from SECTION where id = ?", id);
    }

    public void deleteAllByLineId(final Long lineId) {
        jdbcTemplate.update("delete from SECTION where line_id = ?", lineId);
    }
}
