package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionEntity;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private RowMapper<SectionEntity> sectionMapper = (rs, rowNum) ->
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

    public SectionEntity save(SectionEntity sectionEntity) {
        SqlParameterSource sqlParameterSource = new BeanPropertySqlParameterSource(sectionEntity);
        Long savedId = simpleJdbcInsert.executeAndReturnKey(sqlParameterSource).longValue();
        return SectionEntity.of(savedId, sectionEntity);
    }

    public List<SectionEntity> findAllByLineId(Long lineId) {
        String sql = "SELECT * FROM SECTION WHERE LINE_ID = ?";
        return jdbcTemplate.query(sql, sectionMapper, lineId);
    }

    public void deleteByStationId(Long leftStationId, Long rightStationId) {
        String sql = "DELETE FROM SECTION WHERE LEFT_STATION_ID = ? AND RIGHT_STATION_ID = ?";
        jdbcTemplate.update(sql, leftStationId, rightStationId);
    }
}
