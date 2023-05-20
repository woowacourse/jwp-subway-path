package subway.dao;

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
                    rs.getLong("line_id"),
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

    public Optional<StationEntity> findById(Long stationId, Long lineId) {
        String sql = "select * from STATION where id = ? and line_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, stationId, lineId));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findByName(String name, Long lineId) {
        String sql = "select id, line_id, name from STATION where name = ? and line_id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name, lineId));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void delete(StationEntity savedStation) {
        String sql = "delete from STATION where name = ? and line_id = ?";
        jdbcTemplate.update(sql, new Object[]{savedStation.getName(), savedStation.getId()});
    }

}
