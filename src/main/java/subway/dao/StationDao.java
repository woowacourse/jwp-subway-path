package subway.dao;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name");
    }

    public Station insert(final Station station) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", station.getName());
        final Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        final String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findById(final Long id) {
        final String sql = "select * from STATION where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(final Station station) {
        final String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{station.getName(), station.getId()});
    }

    public void deleteById(final Long id) {
        final String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
