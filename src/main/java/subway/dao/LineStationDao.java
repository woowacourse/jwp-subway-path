package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.LineStation;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class LineStationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<LineStation> rowMapper = (rs, rowNum) ->
            new LineStation(
                    rs.getLong("id"),
                    rs.getLong("up_bound_id"),
                    rs.getLong("down_bound_id"),
                    rs.getLong("line_id"),
                    rs.getInt("distance")
            );

    public LineStationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("line_station")
                .usingGeneratedKeyColumns("id");
    }

    public LineStation insert(LineStation lineStation) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(lineStation);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new LineStation(
                id,
                lineStation.getUpBoundId(),
                lineStation.getDownBoundId(),
                lineStation.getLineId(),
                lineStation.getDistance());
    }

    public List<LineStation> findByLine(Long lineId) {
        String sql = "select * from LINE_STATION where line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public void update(LineStation lineStation) {
        String sql = "update LINE_STATION set up_bound_id = ?, down_bound_id = ?, distance = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{
                lineStation.getUpBoundId(),
                lineStation.getDownBoundId(),
                lineStation.getDistance(),
                lineStation.getId()});
    }

    public void deleteById(Long id) {
        String sql = "delete from LINE_STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
