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
import subway.domain.section.Section;
import subway.domain.station.Station;

@Repository
public class MySqlSectionDao implements SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private static final RowMapper<SectionEntity> rowMapper = (rs, rowNum) -> new SectionEntity(
            rs.getLong("id"),
            rs.getLong("line_id"),
            new Station(rs.getString("start_station_name")),
            new Station(rs.getString("end_station_name")),
            rs.getInt("distance"));

    public MySqlSectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public long insert(Long lineId, Section section) {
        return insertAction.executeAndReturnKey(Map.of(
                "line_id", lineId,
                "start_station_name", section.getUpBoundStation().getName(),
                "end_station_name", section.getDownBoundStation().getName(),
                "distance", section.getDistance()
        )).longValue();
    }

    @Override
    public void update(Long sectionId, Section section) {
        String sql = "UPDATE SECTION SET start_station_name = ?, end_station_name = ?, distance = ? WHERE id = ?";
        jdbcTemplate.update(sql, section.getUpBoundStationName(), section.getDownBoundStationName(),
                section.getDistance(), sectionId);
    }

    @Override
    public Long countByLineId(Long lineId) {
        String sql = "SELECT count(*) FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, lineId);
    }

    @Override
    public List<SectionEntity> findAllByLineId(Long lineId) {
        String sql = "SELECT id, line_id, start_station_name, end_station_name, distance FROM SECTION WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    @Override
    public boolean isStationInLine(Long lineId, String stationName) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE start_station_name = ? OR end_station_name = ? AND line_id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, stationName, stationName, lineId) > 0;
    }

    @Override
    public boolean isEmptyByLineId(Long lineId) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE line_id = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, lineId) == 0;
    }

    @Override
    public Optional<SectionEntity> findByStartStationNameAndLineId(String upBoundStationName, Long lineId) {
        String sql = "SELECT id, line_id, start_station_name, end_station_name, distance FROM SECTION WHERE start_station_name = ? AND line_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, upBoundStationName, lineId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<SectionEntity> findByEndStationNameAndLineId(String downBoundStationName, Long lineId) {
        String sql = "SELECT id, line_id, start_station_name, end_station_name, distance FROM SECTION WHERE end_station_name = ? AND line_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, downBoundStationName, lineId));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteBy(SectionEntity sectionEntity) {
        String sql = "DELETE FROM SECTION WHERE id = ?";
        jdbcTemplate.update(sql, sectionEntity.getId());
    }

    @Override
    public List<SectionEntity> findAll() {
        String sql = "SELECT id, line_id, start_station_name, end_station_name, distance FROM SECTION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public boolean doesNotExistByStationName(String stationName) {
        String sql = "SELECT COUNT(*) FROM SECTION WHERE start_station_name = ? OR end_station_name = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, stationName, stationName) <= 0;
    }
}
