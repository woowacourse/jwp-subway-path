package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.subway.Section;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<Section> rowMapper = (rs, rowNum) ->
        new Section(
            rs.getLong("id"),
            rs.getLong("source_station_id"),
            rs.getLong("target_station_id"),
            rs.getLong("line_id"),
            rs.getInt("distance")
        );

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("section")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(Section section) {
        Map<String, Object> params = new HashMap<>();
        params.put("source_station_id", section.getSourceStationId());
        params.put("target_station_id", section.getTargetStationId());
        params.put("line_id", section.getLineId());
        params.put("distance", section.getDistance());

        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<Section> findAll() {
        String sql = "select * from SECTION";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<Section> findAllSectionByLineId(Long lineId) {
        String sql = "select * from SECTION where line_id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public Section findById(Long sectionId) {
        String sql = "select * from SECTION where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, sectionId);
    }

    public void deleteById(Long id) {
        String sql = "delete from SECTION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
