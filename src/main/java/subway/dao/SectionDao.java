package subway.dao;

import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.dto.SectionDto;
import subway.dao.entity.SectionEntity;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private static final RowMapper<SectionEntity> rowMapper = (rs, rowNum) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            rs.getLong("start_station_id"),
            rs.getLong("end_station_id"),
            rs.getInt("distance"));

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public long insert(SectionEntity sectionEntity) {
        return insertAction.executeAndReturnKey(Map.of(
                "line_id", sectionEntity.getLineId(),
                "start_station_id", sectionEntity.getStartStationId(),
                "end_station_id", sectionEntity.getEndStationId(),
                "distance", sectionEntity.getDistance()
        )).longValue();
    }

    public void update(SectionEntity section) {
        String sql = "UPDATE SECTION SET start_station_id = ?, end_station_id = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql, section.getStartStationId(), section.getEndStationId(),
                section.getDistance(), section.getId());
    }

    public Long countByLineId(Long lineId) {
        String sql = "SELECT count(*) FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, lineId);
    }

    public List<SectionEntity> findAllByLineId(Long lineId) { // TODO: 2023-05-21 test에서만 사용 -> 삭제
        String sql = "SELECT id, line_id, start_station_id, end_station_id, distance FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public List<SectionDto> findAllSectionsByLineId(Long lineId) {
        String sql = "SELECT section.id AS id, start_station.id AS start_station_id, end_station.id AS end_station_id, "
                + "start_station.name AS start_station_name, end_station.name AS end_station_name, section.distance FROM section "
                + "JOIN station AS start_station ON section.start_station_id = start_station.id "
                + "JOIN station AS end_station ON section.end_station_id = end_station.id WHERE section.line_id = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new SectionDto(
                        rs.getLong("id"),
                        rs.getLong("start_station_id"),
                        rs.getLong("end_station_id"),
                        rs.getString("start_station_name"),
                        rs.getString("end_station_name"),
                        rs.getInt("distance")
                ), lineId);
    }

    public List<SectionDto> findAllSectionsWithStationName() {
        String sql = "SELECT section.id AS id, start_station.id AS start_station_id, end_station.id AS end_station_id, "
                + "start_station.name AS start_station_name, end_station.name AS end_station_name, section.distance FROM section "
                + "JOIN station AS start_station ON section.start_station_id = start_station.id "
                + "JOIN station AS end_station ON section.end_station_id = end_station.id";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new SectionDto(
                        rs.getLong("id"),
                        rs.getLong("start_station_id"),
                        rs.getLong("end_station_id"),
                        rs.getString("start_station_name"),
                        rs.getString("end_station_name"),
                        rs.getInt("distance")
                ));
    }

    public boolean isStationInLineById(Long lineId, long stationId) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE start_station_id = ? OR end_station_id = ? AND line_id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, stationId, stationId, lineId) > 0;
    }

    public boolean isEmptyByLineId(Long lineId) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE line_id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, lineId) == 0;
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM SECTION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
