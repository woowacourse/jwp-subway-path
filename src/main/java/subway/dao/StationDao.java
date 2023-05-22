package subway.dao;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private static final RowMapper<StationEntity> ROW_MAPPER = (rs, rowNum) ->
        new StationEntity(
            rs.getLong("id"),
            rs.getString("name")
        );


    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .usingGeneratedKeyColumns("id")
            .withTableName("STATION");
    }

    public Long insert(StationEntity station) {
        return insertAction.executeAndReturnKey(Map.of("name", station.getName()))
            .longValue();
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, ROW_MAPPER);
    }

    public Optional<StationEntity> findByName(String stationName) {
        String sql = "select * from STATION where name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, stationName));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findById(long id) {
        String sql = "SELECT * FROM STATION WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ROW_MAPPER, id));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(StationEntity station) {
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, station.getName(), station.getId());
    }

    public void deleteById(long stationId) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, stationId);
    }

    public boolean existsByName(String stationName) {
        String sql = "SELECT COUNT(*) FROM STATION WHERE name = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, stationName) > 0;
    }
}
