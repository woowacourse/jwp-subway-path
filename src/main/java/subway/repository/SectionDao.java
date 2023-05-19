package subway.repository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.SectionDetailEntity;
import subway.entity.SectionEntity;
import subway.exception.DuplicatedSectionException;
import subway.exception.LineNotFoundException;
import subway.exception.LineOrStationNotFoundException;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static subway.entity.RowMapperUtil.sectionDetailRowMapper;
import static subway.entity.RowMapperUtil.sectionEntityRowMapper;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public SectionDao(final DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public SectionEntity insert(final SectionEntity sectionEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(sectionEntity);
        try {
            Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
            return SectionEntity.of(id, sectionEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicatedSectionException();
        }
    }

    public List<SectionEntity> findByLineName(final String lineName) {
        final String sql = "SELECT se.id, se.line_id, se.distance, se.previous_station_id, se.next_station_id " +
                "FROM section se " +
                "JOIN line ON se.line_id = line.id " +
                "WHERE line.name = ?";
        final List<SectionEntity> result = jdbcTemplate.query(sql, sectionEntityRowMapper, lineName);
        if (result.isEmpty()) {
            throw new LineNotFoundException();
        }
        return result;
    }

    public void delete(final SectionEntity sectionEntity) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, sectionEntity.getId());
    }

    public void deleteAll(final List<SectionEntity> sectionEntities) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(final PreparedStatement ps, final int i) throws SQLException {
                ps.setLong(1, sectionEntities.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return sectionEntities.size();
            }
        });
    }

    public Optional<SectionEntity> findByLineIdAndPreviousStationId(
            final long lineId, final long previousStationId) {
        final String sql = "SELECT * FROM section WHERE line_id = ? AND previous_station_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, lineId, previousStationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<SectionEntity> findByLineIdAndNextStationId(
            final long lineId, final long nextStationId) {
        final String sql = "SELECT * FROM section WHERE line_id = ? AND next_station_id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, sectionEntityRowMapper, lineId, nextStationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<SectionEntity> findByLineIdAndPreviousStationNameOrNextStationName(
            final long lineId, final String stationName) {
        final String sql = "SELECT se.id, se.line_id, se.distance, se.previous_station_id, se.next_station_id " +
                "FROM section se " +
                "JOIN station pst ON se.previous_station_id = pst.id " +
                "JOIN station nst ON se.next_station_id = nst.id " +
                "WHERE se.line_id = ? AND (pst.name = ? OR nst.name = ?)";
        final List<SectionEntity> result = jdbcTemplate.query(sql, sectionEntityRowMapper, lineId, stationName, stationName);
        validateFindByLineIdAndPreviousStationNameOrNextStationNameResult(result);
        return result;
    }

    private void validateFindByLineIdAndPreviousStationNameOrNextStationNameResult(final List<SectionEntity> sectionEntities) {
        if (sectionEntities.isEmpty()) {
            throw new LineOrStationNotFoundException();
        }
        if (sectionEntities.size() > 2) {
            throw new RuntimeException("한 노선에 어떤 역이 2개 이상 존재합니다.");
        }
    }

    public boolean isStationExistInLine(final long lineId, final long stationId) {
        return !findByLineIdAndPreviousStationIdOrNextStationId(lineId, stationId).isEmpty();
    }

    private List<SectionEntity> findByLineIdAndPreviousStationIdOrNextStationId(final long lineId, final long stationId) {
        final String sql = "SELECT * FROM section " +
                "WHERE line_id = ? AND (previous_station_id = ? OR next_station_id = ?)";
        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId, stationId, stationId);
    }

    public List<SectionDetailEntity> findSectionDetail() {
        final String sql = "SELECT se.id, se.distance, se.line_id, " +
                "line.name line_name, line.color line_color, " +
                "pst.id previous_station_id, pst.name previous_station_name, " +
                "nst.id next_station_id, nst.name next_station_name " +
                "FROM section se " +
                "JOIN station pst ON se.previous_station_id = pst.id " +
                "JOIN station nst ON se.next_station_id = nst.id " +
                "JOIN line ON se.line_id = line.id " +
                "WHERE line.id = se.line_id";

        return jdbcTemplate.query(sql, sectionDetailRowMapper);
    }

    public SectionDetailEntity findSectionDetailById(final long id) {
        final String sql = "SELECT se.id, se.distance, se.line_id, " +
                "line.name line_name, line.color line_color, " +
                "pst.id previous_station_id, pst.name previous_station_name, " +
                "nst.id next_station_id, nst.name next_station_name " +
                "FROM section se " +
                "JOIN station pst ON se.previous_station_id = pst.id " +
                "JOIN station nst ON se.next_station_id = nst.id " +
                "JOIN line ON se.line_id = line.id " +
                "WHERE se.id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, sectionDetailRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("존재하지 않는 section id 입니다.");
        }
    }

    public List<SectionDetailEntity> findSectionDetailByLineId(final long lineId) {
        final String sql = "SELECT se.id, se.distance, se.line_id, " +
                "line.name line_name, line.color line_color, " +
                "pst.id previous_station_id, pst.name previous_station_name, " +
                "nst.id next_station_id, nst.name next_station_name " +
                "FROM section se " +
                "JOIN station pst ON se.previous_station_id = pst.id " +
                "JOIN station nst ON se.next_station_id = nst.id " +
                "JOIN line ON se.line_id = line.id " +
                "WHERE se.line_id = ?";

        final List<SectionDetailEntity> result = jdbcTemplate.query(sql, sectionDetailRowMapper, lineId);
        if (result.isEmpty()) {
            throw new LineNotFoundException();
        }
        return result;
    }
}
