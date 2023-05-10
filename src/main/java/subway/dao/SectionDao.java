package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.Section;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Section> rowMapper = (rs, rowNum) ->
            new Section(
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

    public Section insert(Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", section.getLineId());
        params.put("origin_id", section.getOriginId());
        params.put("destination_id", section.getDestinationId());
        params.put("distance", section.getDistance());

        Long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new Section(sectionId, section.getLineId(), section.getOriginId(), section.getDestinationId(),
                section.getDistance());
    }

    public List<Section> findAll() {
        String sql = "SELECT id, line_id, origin_id, destination_id, distance FROM section";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Section findById(Long id) {
        String sql = "SELECT id, line_id, origin_id, destination_id, distance FROM section WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(Section newSection) {
        String sql = "UPDATE section SET line_id = ? , origin_id = ?, destination_id = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                new Object[]{newSection.getLineId(), newSection.getOriginId(), newSection.getDestinationId(),
                        newSection.getDistance(), newSection.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM section WHERE id = ?", id);
    }
}
