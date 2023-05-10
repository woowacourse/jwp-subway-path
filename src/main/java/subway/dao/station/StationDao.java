package subway.dao.station;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.domain.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("station_id"),
                    rs.getString("name")
            );


    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("station_id");
    }

    public boolean isExistStationByName(final String stationName) {
        String sql = "SELECT EXISTS(SELECT 1 FROM station WHERE name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, stationName));
    }

    public StationEntity insert(final Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, station.getName());
    }

    public List<StationEntity> findAll() {
        String sql = "SELECT station_id, name FROM station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public StationEntity findById(final Long id) {
        String sql = "SELECT station_id, name FROM station WHERE station_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public StationEntity findByName(final String name) {
        String sql = "SELECT station_id, name FROM station WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM station WHERE station_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
