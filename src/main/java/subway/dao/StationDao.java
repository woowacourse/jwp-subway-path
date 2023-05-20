package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

@Repository
public class StationDao {
    private static final RowMapper<StationEntity> ENTITY_MAPPER = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    @Autowired
    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(final String name) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        final Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, name);
    }

    public List<StationEntity> findAll() {
        final String sql = "SELECT id, name FROM station";
        return jdbcTemplate.query(sql, ENTITY_MAPPER);
    }

    public Optional<StationEntity> findById(final Long id) {
        final String sql = "SELECT id, name FROM station where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ENTITY_MAPPER, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findByName(final String name) {
        final String sql = "SELECT id, name FROM station where name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, ENTITY_MAPPER, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
