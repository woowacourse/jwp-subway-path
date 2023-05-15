package subway.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.SectionDao;
import subway.domain.line.Line;
import subway.entity.SectionEntity;

import java.util.Optional;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<SectionEntity> rowMapper = (rs, lowNum) ->
            new SectionEntity(
                    rs.getLong("ID"),
                    rs.getLong("LINE_ID"),
                    rs.getLong("LEFT_STATION_ID"),
                    rs.getLong("RIGHT_STATION_ID"),
                    rs.getInt("DISTANCE"));

    public SectionRepository(final SectionDao sectionDao, final JdbcTemplate jdbcTemplate) {
        this.sectionDao = sectionDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public SectionEntity insert(final SectionEntity sectionEntity) {
        return sectionDao.insert(sectionEntity);
    }

    public Optional<SectionEntity> findByLineIdAndLeftStationIdAndRightStationId(final Long lineId, final Long leftStationId,
                                                                                 final Long rightStationId) {
        String sql =
                "SELECT ID, LINE_ID, LEFT_STATION_ID, RIGHT_STATION_ID, DISTANCE "
                        + "FROM SECTION "
                        + "WHERE LINE_ID = ? AND LEFT_STATION_ID = ? AND RIGHT_STATION_ID = ?";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, lineId, leftStationId, rightStationId));
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return Optional.empty();
        }
    }

    public void deleteBySectionId(final Long sectionId) {
        sectionDao.deleteById(sectionId);
    }

    public void insertReCalculateSection(final SectionEntity leftSection, final SectionEntity rightSection) {
        sectionDao.insert(leftSection);
        sectionDao.insert(rightSection);
    }

    public void deleteById(final Long sectionId) {
        String sql = "DELETE FROM SECTION WHERE id = ?";
        jdbcTemplate.update(sql, sectionId);
    }

    public Optional<SectionEntity> findByStationId(final Long stationId) {
        String sql = "SELECT id, line_id, left_station_id, right_station_id, distance FROM SECTION "
                + "WHERE left_station_id = ? OR right_station_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, stationId, stationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteAllSectionsByLine(final Line line) {
        sectionDao.deleteAllSectionsByLineId(line.getId());
    }

    public SectionEntity findLeftByLineIdAndStationId(Long lineId, Long rightStationId) {
        String sql =
                "SELECT S.id, S.line_id, S.left_station_id, S.right_station_id, S.distance FROM SECTION S "
                        + "WHERE line_id = ? AND S.right_station_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, lineId, rightStationId);
    }

    public SectionEntity findRightByLineIdAndStationId(Long lineId, Long leftStationId) {
        String sql =
                "SELECT S.id, S.line_id, S.left_station_id, S.right_station_id, S.distance FROM SECTION S "
                        + "WHERE line_id = ? AND S.left_station_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, lineId, leftStationId);
    }
}
