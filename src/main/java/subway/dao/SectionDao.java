package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.SectionWithStationNameEntity;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<SectionWithStationNameEntity> rowMapper = (rs, rowNum) ->
            new SectionWithStationNameEntity(
                    rs.getLong("line_id"),
                    rs.getString("pre_station_name"),
                    rs.getString("station_name"),
                    rs.getLong("distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public List<SectionWithStationNameEntity> findAllByLineId(Long id) {
        String sql = "SELECT line_id, s1.name AS pre_station_name, s2.name AS station_name, se.distance\n" +
                "FROM section se\n" +
                "JOIN station s1 ON se.pre_station_id = s1.id\n" +
                "JOIN station s2 ON se.station_id = s2.id\n" +
                "WHERE se.line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, id);
    }

    public List<SectionWithStationNameEntity> findAll() {
        String sql = "SELECT line_id, s1.name AS pre_station_name, s2.name AS station_name, se.distance\n" +
                "FROM section se\n" +
                "JOIN station s1 ON se.pre_station_id = s1.id\n" +
                "JOIN station s2 ON se.station_id = s2.id\n";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Long save(SectionEntity section) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(section);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public int updatePreStation(SectionEntity section) {
        String sql = "UPDATE section SET pre_station_id = ? WHERE station_id = ?";
        return jdbcTemplate.update(sql, section.getPreStationId(), section.getStationId());
    }

    public int updateStation(SectionEntity section) {
        String sql = "UPDATE section SET station_id = ? WHERE pre_station_id = ?";
        return jdbcTemplate.update(sql, section.getStationId(), section.getPreStationId());
    }

    public int remove(Long stationId) {
        String sql = "DELETE FROM section WHERE pre_station_id = ? OR station_id = ?";
        return jdbcTemplate.update(sql, stationId, stationId);
    }
}
