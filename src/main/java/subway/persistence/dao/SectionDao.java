package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.dao.entity.SectionEntity;

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
                    rs.getInt("up_station_id"),
                    rs.getInt("down_station_id"),
                    rs.getInt("distance"),
                    rs.getInt("line_id")
            );


    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(SectionEntity sectionEntity, long lineId) {
        Map<String, Object> params = new HashMap<>();
        params.put("up_station_id", sectionEntity.getUpStationId());
        params.put("down_station_id", sectionEntity.getDownStationId());
        params.put("distance", sectionEntity.getDistance());
        params.put("line_id", lineId);

        long sectionId = insertAction.executeAndReturnKey(params).longValue();
        return new SectionEntity(sectionId, sectionEntity.getUpStationId(), sectionEntity.getDownStationId(), sectionEntity.getDistance(), lineId);
    }

    public List<SectionEntity> findSectionsByLine(long lineId) {
        String sql = "SELECT * FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }
}
