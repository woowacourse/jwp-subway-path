package subway.dao;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.domain.Section;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

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

    public void update(Long sectionId, Section section) {
        String sql = "UPDATE SECTION SET start_station_name = ?, end_station_name = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql, section.getStartStation().getName(), section.getEndStation().getName(), section.getDistance(), sectionId);
    }

    public Long countByLineId(Long lineId) {
        String sql = "SELECT count(*) FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class ,lineId);
    }

    public List<SectionEntity> findAllByLineId(Long lineId) {
        String sql = "SELECT id, line_id, start_station_name, end_station_name, distance FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new SectionEntity(
                rs.getLong("id"),
                rs.getLong("line_id"),
                rs.getString("start_station_name"),
                rs.getString("end_station_name"),
                rs.getInt("distance")), lineId);
    }

    public boolean existsByStartStationNameAndLineId(String stationName, Long lineId) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE start_station_name = ? AND line_id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, stationName, lineId) > 0;
    }

    public boolean existsByEndStationNameAndLineId(String stationName, Long lineId) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE end_station_name = ? AND line_id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, stationName, lineId) > 0;
    }

    public boolean isEmptyByLineId(Long lineId) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE line_id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Integer.class, lineId) == 0;

    }
}
