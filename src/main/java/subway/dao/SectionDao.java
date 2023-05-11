package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.rowmapper.SectionDetail;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

import static subway.dao.rowmapper.util.RowMapperUtil.*;

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

    // TODO: 없는 호선이나 역을 입력한 경우 예외 처리 해야함
    public SectionEntity insert(final SectionEntity sectionEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(sectionEntity);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return new SectionEntity.Builder()
                .id(id)
                .lineId(sectionEntity.getLineId())
                .previousStationId(sectionEntity.getPreviousStationId())
                .nextStationId(sectionEntity.getNextStationId())
                .distance(sectionEntity.getDistance())
                .build();
    }

    public List<SectionEntity> findByLineIdAndPreviousStationId(final Long lineId, final Long previousStationId) {
        final String sql = "SELECT * FROM section WHERE line_id = ? AND previous_station_id = ?";

        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId, previousStationId);
    }

    public List<SectionEntity> findByLineIdAndNextStationId(final Long lineId, final Long nextStationId) {
        final String sql = "SELECT * FROM section WHERE line_id = ? AND next_station_id = ?";

        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId, nextStationId);
    }

    public List<SectionEntity> findByLineId(final Long lineId) {
        final String sql = "SELECT * " +
                "FROM section WHERE line_id = ?";

        return jdbcTemplate.query(sql, sectionEntityRowMapper, lineId);
    }

    public List<SectionEntity> findAll() {
        final String sql = "SELECT * FROM section";

        return jdbcTemplate.query(sql, sectionEntityRowMapper);
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

    public List<StationEntity> findStationByLineId(final long lineId) {
        final String sql = "SELECT DISTINCT station.id, station.name " +
                "FROM section JOIN station " +
                "ON section.previous_station_id = station.id " +
                "OR section.next_station_id = station.id " +
                "WHERE section.line_id = ?";

        return jdbcTemplate.query(sql, stationEntityRowMapper, lineId);
    }

    public List<SectionDetail> findSectionDetailByLineId(final long lineId) {
        final String sql = "SELECT se.id, se.distance, se.line_id, " +
                "line.name line_name, line.color line_color, " +
                "pst.id previous_station_id, pst.name previous_station_name, " +
                "nst.id next_station_id, nst.name next_station_name " +
                "FROM section se " +
                "JOIN station pst ON se.previous_station_id = pst.id " +
                "JOIN station nst ON se.next_station_id = nst.id " +
                "JOIN line " +
                "WHERE se.line_id = ?";

        return jdbcTemplate.query(sql, sectionDetailRowMapper, lineId);
    }

    public List<SectionDetail> findSectionDetail() {
        final String sql = "SELECT se.id, se.distance, se.line_id, " +
                "line.name line_name, line.color line_color, " +
                "pst.id previous_station_id, pst.name previous_station_name, " +
                "nst.id next_station_id, nst.name next_station_name " +
                "FROM section se " +
                "JOIN station pst ON se.previous_station_id = pst.id " +
                "JOIN station nst ON se.next_station_id = nst.id " +
                "JOIN line " +
                "WHERE line.id = se.line_id";

        return jdbcTemplate.query(sql, sectionDetailRowMapper);
    }
}
