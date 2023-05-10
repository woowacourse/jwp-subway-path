package subway.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.domain.Section;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private static final RowMapper<SectionEntity> rowMapper = (rs, rowNum) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getString("start_station_name"),
            rs.getString("end_station_name"),
            rs.getInt("distance"));

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(Long lineId, Section section) {
        return insertAction.executeAndReturnKey(Map.of(
                "line_id", lineId,
                "start_station_name", section.getStartStation().getName(),
                "end_station_name", section.getEndStation().getName(),
                "distance", section.getDistance()
        )).longValue();
    }

    public void update(SectionEntity sectionEntity) {
        String sql = "UPDATE SECTION SET start_station_name = ?, end_station_name = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql, sectionEntity.getStartStationName(), sectionEntity.getEndStationName(),
                sectionEntity.getDistance(), sectionEntity.getId());
    }

    public Long countByLineId(Long lineId) {
        String sql = "SELECT count(*) FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, lineId);
    }

    public List<SectionEntity> findAllByLineId(Long lineId) {
        String sql = "SELECT id, line_id, start_station_name, end_station_name, distance FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public boolean isStationInLine(Long lineId, String stationName) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE start_station_name = ? OR end_station_name = ? AND line_id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, stationName, stationName, lineId) > 0;
    }

    public boolean isEmptyByLineId(Long lineId) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE line_id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, lineId) == 0;
    }

    public Optional<SectionEntity> findByStartStationNameAndLineId(String startStationName, Long lineId) {
        String sql = "SELECT id, line_id, start_station_name, end_station_name, distance FROM SECTION WHERE start_station_name = ? AND line_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, startStationName, lineId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<SectionEntity> findByEndStationNameAndLineId(String endStationName, Long lineId) {
        String sql = "SELECT id, line_id, start_station_name, end_station_name, distance FROM SECTION WHERE end_station_name = ? AND line_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, endStationName, lineId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }
}
