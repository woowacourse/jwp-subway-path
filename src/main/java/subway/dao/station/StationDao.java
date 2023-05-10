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

    private RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("station_id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("station_id");
    }

    public boolean isExistStationByName(final String stationName) {
        String sql = "SELECT EXISTS(SELECT 1 FROM station WHERE name = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, stationName));
    }

    public StationEntity insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, station.getName());
    }

    public List<StationEntity> findAll() {
        String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public StationEntity findById(Long id) {
        String sql = "SELECT * FROM station WHERE station_id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public StationEntity findByName(final String name) {
        String sql = "SELECT * FROM station WHERE name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public void update(Station newStation) {
//        String sql = "UPDATE station SET name = ? WHERE id = ?";
//        jdbcTemplate.update(sql, new Object[]{newStation.getName(), newStation.getId()});
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM station WHERE station_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
