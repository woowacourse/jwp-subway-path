package subway.dao;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Long insert(StationEntity stationEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public StationEntity findById(Long id) {
        String sql = "select * from STATION where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Boolean isExistStationById(Long id) {
        String sql = "select exists(select * from station where id = ?)";

        return jdbcTemplate.queryForObject(sql, Boolean.class, id);
    }

    public List<StationEntity> findStations(List<String> stations) {
        String sql = "select * from station where station.name in (:name)";

        SqlParameterSource parameters = new MapSqlParameterSource("name",stations);

        return namedParameterJdbcTemplate.query(sql, parameters, rowMapper);
    }

    public List<StationEntity> findAll() {
        String sql = "select * from station";

        return jdbcTemplate.query(sql,rowMapper);
    }
}
