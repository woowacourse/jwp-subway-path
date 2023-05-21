package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;
import subway.exception.NoSuchStationException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class H2StationDao implements StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public H2StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public StationEntity insert(final StationEntity stationEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, stationEntity.getName());
    }

    @Override
    public List<StationEntity> findAll() {
        String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<StationEntity> findById(final Long id) {
        String sql = "SELECT * FROM station WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
    }

    @Override
    public void update(final StationEntity newStationEntity) {
        if (!existsById(newStationEntity.getId())) {
            throw new NoSuchStationException(newStationEntity.getId());
        }
        String sql = "UPDATE station SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, newStationEntity.getName(), newStationEntity.getId());
    }

    @Override
    public void deleteById(final Long id) {
        if (!existsById(id)) {
            throw new NoSuchStationException(id);
        }
        String sql = "DELETE FROM station WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private boolean existsById(final Long id) {
        String sql = "SELECT EXISTS(SELECT 1 FROM station WHERE id = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, Boolean.class);
    }
}
