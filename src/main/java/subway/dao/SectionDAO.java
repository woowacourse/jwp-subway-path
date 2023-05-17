package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;

@Repository
public class SectionDAO {
    
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<Section> rowMapper = ((rs, rowNum) -> {
        long id = rs.getLong("id");
        long lineId = rs.getLong("line_id");
        long upStationId = rs.getLong("up_station_id");
        long downStationId = rs.getLong("down_station_id");
        int distance = rs.getInt("distance");
        return new Section(id, lineId, upStationId, downStationId, distance);
    });
    
    public SectionDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }
    
    public Section insert(final Section section) {
        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(section);
        final long sectionId = this.insertAction.executeAndReturnKey(source).longValue();
        return new Section(sectionId, section.getLineId(), section.getUpStationId(), section.getDownStationId(),
                section.getDistance());
    }
    
    public List<Section> findSectionsBy(final long baseStationId, final long lineId) {
        final String sql = "select * from section where (up_station_id = ? or down_station_id = ?) and line_id = ?";
        return this.jdbcTemplate.query(sql, this.rowMapper, baseStationId, baseStationId, lineId);
    }
    
    public void deleteById(final long id) {
        final String sql = "delete from section where id = ?";
        this.jdbcTemplate.update(sql, id);
    }
    
    public List<Section> findSectionsBy(final long lineId) {
        final String sql = "select * from section where line_id = ?";
        return this.jdbcTemplate.query(sql, this.rowMapper, lineId);
    }
    
    public List<Section> findAll() {
        final String sql = "select * from section";
        return this.jdbcTemplate.query(sql, this.rowMapper);
    }
}
