package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dto.SectionRequest;

import java.util.HashMap;
import java.util.Map;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final SectionRequest sectionRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", sectionRequest.getLineId());
        params.put("up_station_id", sectionRequest.getUpStationId());
        params.put("down_station_id", sectionRequest.getStationId());
        params.put("distance", sectionRequest.getDistance());
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public void deleteByStationId(final Long stationId) {
        String sql = "DELETE FROM section WHERE down_station_id = ?";
        jdbcTemplate.update(sql, stationId);
    }
}

