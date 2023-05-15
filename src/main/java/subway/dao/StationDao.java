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

    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "select * from STATION where id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(StationEntity newStation) {
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newStation.getName(), newStation.getId()});
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<StationEntity> findByName(String name, Long lineId) {
        String sql = "select * from STATION where name = ? and line_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name, lineId));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<StationEntity> findByLineId(Long lineId) {
        String sql = "select * from STATION where line_id = ?";
        return jdbcTemplate.query(sql, rowMapper, lineId);
    }

    public Optional<StationEntity> findByStationAndLineId(Long stationId, Long lineId) {
        String sql = "select * from STATION where station_id =? and line_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, stationId, lineId));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteByStationAndLineId(Long stationId, Long lineId) {
        String sql = "delete from STATION where station_id =? and line_id = ?";
        jdbcTemplate.update(sql, stationId, lineId);
    }
}
