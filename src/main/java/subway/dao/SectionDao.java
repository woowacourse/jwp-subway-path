package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.dto.SectionStationResultMap;
import subway.dao.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final RowMapper<SectionEntity> rowMapper = (rs, num) -> new SectionEntity(
            rs.getLong("id"),
            rs.getInt("distance"),
            rs.getLong("up_station_id"),
            rs.getLong("down_station_id"),
            rs.getLong("line_id")
    );

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(SectionEntity section) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(section);
        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    // TODO 분리하기
    public List<SectionStationResultMap> findAllByLineId(Long lineId) {
        final String sql = "SELECT se.id sectionId, " +
                "se.distance distance, " +
                "se.up_station_id, " +
                "st1.name upStationName, " +
                "se.down_station_id, " +
                "st2.name downStationName, " +
                "se.line_id lineId " +
                "FROM section se " +
                "JOIN station st1 ON st1.id = se.up_station_id " +
                "JOIN station st2 ON st2.id = se.down_station_id " +
                "WHERE line_id = ?";
        final RowMapper<SectionStationResultMap> resultMapRowMapper = (rs, num) -> new SectionStationResultMap(
                rs.getLong("sectionId"),
                rs.getInt("distance"),
                rs.getLong("up_station_id"),
                rs.getString("upStationName"),
                rs.getLong("down_station_id"),
                rs.getString("downStationName"),
                rs.getLong("lineId")
        );
        return jdbcTemplate.query(sql, resultMapRowMapper, lineId);
    }

    public void update(SectionEntity updateSection) {
        final String sql = "UPDATE section SET distance = ?, up_station_id = ?, down_station_id = ?";
        jdbcTemplate.update(sql, updateSection.getDistance(), updateSection.getUpStationId(), updateSection.getDownStationId());
    }

    public boolean exists(Long upStationId, Long downStationId) {
        final String sql = "SELECT EXISTS (SELECT 1 FROM section WHERE up_station_id = ? AND down_station_id = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, upStationId, downStationId);
    }

    public void delete(Long id) {
        final String sql = "DELETE FROM section WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
