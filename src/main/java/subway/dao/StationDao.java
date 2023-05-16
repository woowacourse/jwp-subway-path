package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;

    private static final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void insert(StationEntity station) {
        String sql = "INSERT INTO STATION (name) VALUES (?)";
        jdbcTemplate.update(sql, station.getName());
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findByName(String stationName) {
        String sql = "select * from STATION where name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, stationName));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public StationEntity findById(Long id) {
        String sql = "SELECT * FROM STATION WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(StationEntity station) {
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, station.getName(), station.getId());
    }

    public void deleteById(Long stationId) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, stationId);
    }

    public boolean existsByName(String stationName) {
        String sql = "SELECT COUNT(*) FROM STATION WHERE name = ? LIMIT 1";
        return jdbcTemplate.queryForObject(sql, Long.class, stationName) > 0;
    }
}
