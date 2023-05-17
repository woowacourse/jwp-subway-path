package subway.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

@Repository
public class StationDao {
    private static final int EXISTED_STATION = 1;
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
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public Optional<StationEntity> findById(final Long id) {
        final String sql = "SELECT * FROM station WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Long save(final StationEntity stationEntity) {
        return insertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(stationEntity)).longValue();
    }

    public boolean exists(final StationEntity stationEntity) {
        final String sql = "SELECT EXISTS(SELECT * FROM station WHERE id = ?)";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class, stationEntity.getId());

        return result == EXISTED_STATION;
    }

    public Optional<StationEntity> findByName(final String name) {
        final String sql = "SELECT * FROM station WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int deleteByName(final String name) {
        final String sql = "DELETE FROM station WHERE name = ?";
        return jdbcTemplate.update(sql, name);
    }
}
