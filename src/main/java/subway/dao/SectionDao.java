package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.dto.SectionRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private RowMapper<Section> rowMapper = (rs, rowNum) ->
            new Section(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("up_station_id"),
                    rs.getLong("down_station_id"),
                    rs.getInt("distance")
            );

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

    public Optional<Long> findById(final Long id) {
        String sql = "SELECT * FROM section WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteByStationId(final Long lineId, final Long stationId) {
        String sql = "DELETE FROM section WHERE line_id = ? AND (up_station_id = ? OR down_station_id = ?)";
        jdbcTemplate.update(sql, lineId, stationId, stationId);
    }

    public List<Section> findByLineIdAndStationId(final Long lineId, final Long stationId) {
        String sql = "SELECT * FROM section WHERE line_id = ? AND (up_station_id = ? OR down_station_id = ?)";
        return jdbcTemplate.query(sql, rowMapper, lineId, stationId, stationId);
    }

    public Optional<Long> findByLineId(final Long lineId) {
        String sql = "SELECT * FROM section WHERE line_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, lineId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}

