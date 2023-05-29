package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<StationEntity> rowMapper = (resultSet, rowNumber) ->
            new StationEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );

    public Long save(final StationEntity stationEntity) {
        final MapSqlParameterSource name = new MapSqlParameterSource()
                .addValue("name", stationEntity.getName());
        return insertAction.executeAndReturnKeyHolder(name).getKey().longValue();
    }

    public List<StationEntity> findAll() {
        String sql = "select * from station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "select * from station where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<StationEntity> findByName(final StationEntity stationEntity) {
        String sql = "select * from station where name = ?";
        return jdbcTemplate.query(sql, rowMapper, stationEntity.getName());
    }

    public void update(final StationEntity stationEntity) {
        final String sql = "UPDATE station SET name = ? WHERE id = ? ";
        jdbcTemplate.update(sql, stationEntity.getName(), stationEntity.getId());
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM station WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
