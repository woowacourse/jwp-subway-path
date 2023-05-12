package subway.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.LineSectionDao;
import subway.dao.SectionDao;
import subway.entity.LineSectionEntity;
import subway.entity.SectionEntity;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final LineSectionDao lineSectionDao;
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<SectionEntity> rowMapper = (rs, lowNum) ->
        new SectionEntity(
            rs.getLong("ID"),
            rs.getLong("LEFT_STATION_ID"),
            rs.getLong("RIGHT_STATION_ID"),
            rs.getInt("DISTANCE"));

    public SectionRepository(final SectionDao sectionDao, final LineSectionDao lineSectionDao, final JdbcTemplate jdbcTemplate) {
        this.sectionDao = sectionDao;
        this.lineSectionDao = lineSectionDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    public SectionEntity insert(final SectionEntity sectionEntity) {
        return sectionDao.insert(sectionEntity);
    }

    public Optional<SectionEntity> findByLineIdAndLeftStationIdAndRightStationId(final Long lineId, final Long leftStationId,
        final Long rightStationId) {
        String sql =
            "SELECT S.ID, S.LEFT_STATION_ID, S.RIGHT_STATION_ID, S.DISTANCE "
                + "FROM SECTION S, LINE_SECTION LS "
                + "WHERE S.ID = LS.SECTION_ID "
                + "AND LS.LINE_ID = ? AND S.LEFT_STATION_ID = ? AND S.RIGHT_STATION_ID = ?";

        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper
                , lineId, leftStationId, rightStationId));
        } catch (EmptyResultDataAccessException | NullPointerException e) {
            return Optional.empty();
        }
    }

    public void deleteSectionAndLineSectionBySectionId(final Long sectionId) {
        sectionDao.deleteById(sectionId);
        lineSectionDao.deleteBySectionId(sectionId);
    }

    public void insertReCalculateSection(final Long lineId, final SectionEntity leftSection, final SectionEntity rightSection) {
        SectionEntity insertedLeftSection = sectionDao.insert(leftSection);
        SectionEntity insertedRightSection = sectionDao.insert(rightSection);
        lineSectionDao.insert(new LineSectionEntity(lineId, insertedLeftSection.getId()));
        lineSectionDao.insert(new LineSectionEntity(lineId, insertedRightSection.getId()));
    }

    public void deleteById(final Long sectionId) {
        String sql = "DELETE FROM SECTION WHERE id = ?";
        jdbcTemplate.update(sql, sectionId);
    }

    public Optional<SectionEntity> findByStationId(final Long stationId) {
        String sql = "SELECT id, left_station_id, right_station_id, distance FROM SECTION "
            + "WHERE left_station_id = ? OR right_station_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, stationId, stationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<SectionEntity> findByLineIdAndStationId(final Long lineId, final Long stationId) {
        String sql =
            "SELECT S.id, S.left_station_id, S.right_station_id, S.distance FROM LINE_SECTION LS, SECTION S "
                + "WHERE LS.section_id = S.ID AND LS.line_id = ? AND (S.left_station_id = ? OR S.right_station_id = ?)";
        return jdbcTemplate.query(sql, rowMapper, lineId, stationId, stationId);
    }
}
