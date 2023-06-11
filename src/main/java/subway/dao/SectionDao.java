package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.SectionEntity;

import java.util.List;

@Component
public class SectionDao {

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

    public SectionDao(final JdbcTemplate jdbcTemplate) {
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
        final String sql = "select id, line_id, up_station_id, down_station_id, distance from section where (up_station_id = ? or down_station_id = ?) and line_id = ?";
        return this.jdbcTemplate.query(sql, this.rowMapper, baseStationId, baseStationId, lineId);
    }

    public void deleteById(final long id) {
        final String sql = "delete from section where id = ?";
        this.jdbcTemplate.update(sql, id);
    }

    public List<SectionEntity> findSectionsBy(final long lineId) {
        final String sql = "select id, line_id, up_station_id, down_station_id,distance from section where line_id = ?";
        return this.jdbcTemplate.query(sql, this.rowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        final String sql = "select id, line_id, up_station_id, down_station_id,distance from section";
        return this.jdbcTemplate.query(sql, this.rowMapper);
    }

    public void deleteByLineId(long lineId) {
        final String sql = "delete from section where line_id = ?";
        this.jdbcTemplate.update(sql, lineId);
    }
}
