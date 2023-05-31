package subway.station.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.station.entity.StationEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity.Builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .build();

    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(StationEntity stationEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", stationEntity.getName());

        long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity.Builder()
                .id(id)
                .name(stationEntity.getName())
                .build();
    }

    public Optional<StationEntity> findById(Long stationId) {
        try {
            String sql = "select id, name from STATION where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findByName(String stationName) {
        try {
            String sql = "select id, name from STATION where name = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationName));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<StationEntity> findAllStations() {
        String sql = "select id, name from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }
}
