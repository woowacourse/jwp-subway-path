package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("stations")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(final StationEntity station) {
        final Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", station.getName());
        return insertAction.executeAndReturnKey(parameters).longValue();
    }

    public List<StationEntity> findAll() {
        String sql = "SELECT * FROM STATIONS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "SELECT * FROM STATIONS WHERE id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final StationEntity station) {
        String sql = "UPDATE STATIONS SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, station.getName(), station.getId());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM STATIONS WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
