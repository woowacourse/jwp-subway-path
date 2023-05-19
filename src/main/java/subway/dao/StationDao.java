package subway.dao;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.entity.StationEntity;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            StationEntity.of(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(Station station) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", station.getName().getName());
        long id = insertAction.executeAndReturnKey(params).longValue();
        return StationEntity.of(id, station);
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public StationEntity findById(Long id) {
        String sql = "select * from STATION where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(Station newStation) {
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, newStation.getName().getName(), newStation.getId());
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public StationEntity findByName(final String name) {
        String sql = "select * from STATION where name = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, name);
    }

    public boolean existsByName(String name) {
        String sql = "SELECT COUNT(*) FROM Station WHERE name = ?";
        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class, name);

        if (integer == null) {
            return false;
        }

        return integer > 0;
    }

    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM Station WHERE id = ?";
        Integer integer = jdbcTemplate.queryForObject(sql, Integer.class, id);

        if (integer == null) {
            return false;
        }

        return integer > 0;
    }
}
