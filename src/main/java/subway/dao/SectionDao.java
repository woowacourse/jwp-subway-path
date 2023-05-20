package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Section;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Section> rowMapper = (rs, rowNum) ->
            new Section(
                    rs.getLong("id"),
                    rs.getLong("up_bound_station_id"),
                    rs.getLong("down_bound_station_id"),
                    rs.getLong("line_id"),
                    rs.getInt("distance")
            );

    public SectionDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    public Section insert(Section section) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(section);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Section(
                id,
                section.getUpBoundStationId(),
                section.getDownBoundStationId(),
                section.getLineId(),
                section.getDistance());
    }

    public List<Section> findByLineId(Long lineId) {
        String sql = "select * from SECTION where line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public Section findByStationsIdAndLineId(Long upBoundStationId, Long downBoundStationId, Long lineId) {
        String sql = "select * from SECTION where up_bound_station_id = ? and down_bound_station_id = ? and line_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, upBoundStationId, downBoundStationId, lineId);
    }

    public List<Section> findAll() {
        String sql = "select * from SECTION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void update(Section lineStation) {
        String sql = "update SECTION set up_bound_station_id = ?, down_bound_station_id = ?, distance = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{
                lineStation.getUpBoundStationId(),
                lineStation.getDownBoundStationId(),
                lineStation.getDistance(),
                lineStation.getId()});
    }

    public void deleteById(Long id) {
        String sql = "delete from SECTION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
