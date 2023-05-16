package subway.persistence;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.exception.DuplicatedSectionException;
import subway.exception.LineNotFoundException;
import subway.persistence.entity.SectionDetailEntity;
import subway.persistence.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

import static subway.persistence.entity.RowMapperUtil.sectionDetailRowMapper;
import static subway.persistence.entity.RowMapperUtil.sectionEntityRowMapper;

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

    public List<SectionEntity> findByLineIdAndPreviousStationIdOrNextStationId(final Long lineId, final Long stationId) {
        final String sql = "SELECT * FROM section WHERE line_id = ? AND previous_station_id = ? OR next_station_id = ?";
        List<SectionEntity> result = jdbcTemplate.query(sql, sectionEntityRowMapper, lineId, stationId, stationId);
        validateSection(result);
        return result;
    }

    private static void validateSection(final List<SectionEntity> result) {
        if (result.isEmpty()) {
            throw new IllegalArgumentException("해당 노선에 해당 역이 존재하지 않습니다.");
        }

        if (result.size() > 2) {
            throw new RuntimeException("간선의 정보가 잘못되었습니다.");
        }
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

    public List<SectionDetailEntity> findSectionDetailByLineName(final String lineName) {
        final String sql = "SELECT se.id, se.distance, se.line_id, " +
                "line.name line_name, line.color line_color, " +
                "pst.id previous_station_id, pst.name previous_station_name, " +
                "nst.id next_station_id, nst.name next_station_name " +
                "FROM section se " +
                "JOIN station pst ON se.previous_station_id = pst.id " +
                "JOIN station nst ON se.next_station_id = nst.id " +
                "JOIN line ON se.line_id = line.id " +
                "WHERE line.name = ?";

        final List<SectionDetailEntity> result = jdbcTemplate.query(sql, sectionDetailRowMapper, lineName);
        if (result.isEmpty()) {
            throw new LineNotFoundException();
        }
        return result;
    }

}
