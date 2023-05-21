package subway.dao;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.exception.station.DuplicateStationNameException;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;

    private RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("id"),
                    rs.getLong("line_id"),
                    rs.getLong("start_station_id"),
                    rs.getLong("end_station_id"),
                    rs.getInt("distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<SectionEntity> findAll() {
        String sql = "select * from SECTION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public List<SectionEntity> findByLineId(Long lineId) {
        String sql = "select * from SECTION where line_id = ?";

        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public boolean existStationByStationId(final Long stationId) {
        String sql = "select exists(select * from SECTION where start_station_id = ? or end_station_id = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, stationId, stationId));
    }

    public void update(final SectionEntity sectionEntity) {
        String sql = "update SECTION set end_station_id = ?, distance = ? where line_id = ? and start_station_id = ?";

        jdbcTemplate.update(sql, sectionEntity.getEndStationId(), sectionEntity.getDistance(),
                sectionEntity.getLineId(), sectionEntity.getStartStationId());
    }

    public void delete(final SectionEntity sectionEntity) {
        String sql = "delete from SECTION where start_station_id = ? and end_station_id = ? and line_id = ?";

        jdbcTemplate.update(sql, sectionEntity.getStartStationId(), sectionEntity.getEndStationId(),
                sectionEntity.getLineId());
    }

    public void insert(final SectionEntity sectionEntity) {
        String sql = "insert into SECTION (line_id, start_station_id, end_station_id, distance) values (?, ?, ?, ?)";

        try {
            jdbcTemplate.update(sql, sectionEntity.getLineId(), sectionEntity.getStartStationId(),
                    sectionEntity.getEndStationId(), sectionEntity.getDistance());
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateStationNameException("시작 역과 도착 역은 같을 수 없습니다.");
        }
    }
}
