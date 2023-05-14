package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;
import subway.entity.SectionEntity;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<SectionEntity> rowMapper = (rs, rowNum) ->
            new SectionEntity(
                    rs.getLong("line_id"),
                    rs.getLong("pre_station_id"),
                    rs.getLong("station_id"),
                    rs.getLong("distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public List<SectionEntity> findAllByLineId(Long id) {
        String sql = "SELECT id, line_id, pre_station_id, station_id, distance FROM section WHERE line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, id);
    }

    public Long save(SectionEntity section) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(section);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public int updatePre(Section modified) {
        String sql = "UPDATE section SET pre_station_id = ? WHERE station_id = ?";
        return jdbcTemplate.update(sql, modified.getPreStationId(), modified.getStationId());
    }

    public int remove(Long stationId) {
        String sql = "DELETE FROM section WHERE pre_station_id = ? OR station_id = ?";
        return jdbcTemplate.update(sql, stationId, stationId);
    }
}
