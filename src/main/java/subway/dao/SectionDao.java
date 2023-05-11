package subway.dao;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.Section;

@Repository
public class SectionDao {

    private RowMapper<Section> sectionRowMapper = (rs, rowNum) -> Section.of(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getInt("distance"));


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("SECTIONS")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", section.getLineId());
        params.put("up_station_id", section.getUpStationId());
        params.put("down_station_id", section.getDownStationId());
        params.put("distance", section.getDistance());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Section findByUpStationId(final Long upStationId) {
        String sql = "select * from SECTIONS where up_station_id = ?";
        return jdbcTemplate.queryForObject(sql, sectionRowMapper, upStationId);
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM SECTIONS WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public Section findByDownStationId(final Long downStationId) {
        String sql = "select * from SECTIONS where down_station_id = ?";
        return jdbcTemplate.queryForObject(sql, sectionRowMapper, downStationId);
    }

    public List<Section> findAll() {
        String sql = "select * from SECTIONS";
        return jdbcTemplate.query(sql, sectionRowMapper);
    }
}
