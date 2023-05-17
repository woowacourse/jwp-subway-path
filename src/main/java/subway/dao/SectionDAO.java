package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

@Repository
public class SectionDAO {
    
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    
    private final RowMapper<SectionEntity> rowMapper = ((rs, rowNum) -> {
        long id = rs.getLong("id");
        long lineId = rs.getLong("line_id");
        long upStationId = rs.getLong("up_station_id");
        long downStationId = rs.getLong("down_station_id");
        int distance = rs.getInt("distance");
        return new SectionEntity(id, lineId, upStationId, downStationId, distance);
    });
    
    public SectionDAO(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }
    
    public SectionEntity insert(final SectionEntity sectionEntity) {
        final BeanPropertySqlParameterSource source = new BeanPropertySqlParameterSource(sectionEntity);
        final long sectionId = this.insertAction.executeAndReturnKey(source).longValue();
        return new SectionEntity(sectionId, sectionEntity.getLineId(), sectionEntity.getUpStationId(), sectionEntity.getDownStationId(),
                sectionEntity.getDistance());
    }
    
    public List<SectionEntity> findSectionsBy(final long baseStationId, final long lineId) {
        final String sql = "select * from section where (up_station_id = ? or down_station_id = ?) and line_id = ?";
        return this.jdbcTemplate.query(sql, this.rowMapper, baseStationId, baseStationId, lineId);
    }
    
    public void deleteById(final long id) {
        final String sql = "delete from section where id = ?";
        this.jdbcTemplate.update(sql, id);
    }
    
    public List<SectionEntity> findSectionsBy(final long lineId) {
        final String sql = "select * from section where line_id = ?";
        return this.jdbcTemplate.query(sql, this.rowMapper, lineId);
    }
}
