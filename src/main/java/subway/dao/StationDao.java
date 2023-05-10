package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.Station;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
            Station.of(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("stations")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<Station> findAll() {
        String sql = "select * from STATIONS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findById(Long id) {
        String sql = "select * from STATIONS where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void updateById(Long id, Station newStation) {
        String sql = "update STATIONS set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newStation.getName(), id});
    }

    public void deleteById(Long id) {
        String sql = "delete from STATIONS where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
