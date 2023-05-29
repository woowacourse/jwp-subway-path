package subway.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.StationEntity;

@Component
public class StationDao {

    private static final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(StationEntity station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Optional<StationEntity> findById(Long stationId) {
        String sql = "select * from STATION where id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, stationId));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findByName(String name) {
        String sql = "select id, name from STATION where name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(Long stationId) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, stationId);
    }

    public List<StationEntity> findAll() {
        String sql = "select id, name, from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public void update(StationEntity stationEntity) {
        String sql = "update STATION set name = ? where id =?";
        jdbcTemplate.update(sql, stationEntity.getName(), stationEntity.getId());
    }
}
