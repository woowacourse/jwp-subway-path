package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<SectionEntity> sectionMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("left_station_id"),
                    rs.getLong("right_station_id"),
                    rs.getInt("distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(SectionEntity sectionEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(sectionEntity);
        long savedId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new SectionEntity(savedId, sectionEntity.getLineId(), sectionEntity.getLeftStationId(),
                sectionEntity.getRightStationId(),
                sectionEntity.getDistance());
    }

    public List<SectionEntity> findByLineId(Long lineId) {
        String sql = "SELECT ID, LINE_ID, LEFT_STATION_ID, RIGHT_STATION_ID, DISTANCE FROM SECTION WHERE LINE_ID = ?";

        return jdbcTemplate.query(sql, sectionMapper, lineId);
    }

    public void deleteByStationId(Long id) {
        String sql = "DELETE FROM SECTION WHERE ID = ?";
        jdbcTemplate.update(sql, id);
    }
}
