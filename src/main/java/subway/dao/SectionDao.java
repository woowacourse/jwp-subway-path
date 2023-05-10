package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("origin_id"),
                    rs.getLong("destination_id"),
                    rs.getInt("distance")
            );


    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(SectionEntity sectionEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", sectionEntity.getLineId());
        params.put("origin_id", sectionEntity.getOriginId());
        params.put("destination_id", sectionEntity.getDestinationId());
        params.put("distance", sectionEntity.getDistance());

        Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(sectionId, sectionEntity.getLineId(), sectionEntity.getOriginId(),
                sectionEntity.getDestinationId(),
                sectionEntity.getDistance());
    }

    public List<SectionEntity> findAll() {
        String sql = "SELECT id, line_id, origin_id, destination_id, distance FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public SectionEntity findById(Long id) {
        String sql = "SELECT id, line_id, origin_id, destination_id, distance FROM section WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(SectionEntity newSectionEntity) {
        String sql = "UPDATE section SET line_id = ? , origin_id = ?, destination_id = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                new Object[]{newSectionEntity.getLineId(), newSectionEntity.getOriginId(),
                        newSectionEntity.getDestinationId(),
                        newSectionEntity.getDistance(), newSectionEntity.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM section WHERE id = ?", id);
    }
}
