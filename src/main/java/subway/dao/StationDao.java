package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class StationDao implements Dao<StationEntity> {

    private static final RowMapper<StationEntity> stationRowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    @Override
    public Long insert(final StationEntity stationEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    @Override
    public Optional<StationEntity> findById(final Long id) {
        final String sql = "SELECT * FROM station WHERE id = ?";
        return findInOptional(jdbcTemplate, sql, stationRowMapper, id);
    }

    @Override
    public List<StationEntity> findAll() {
        final String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, stationRowMapper);
    }

    @Override
    public void update(final StationEntity stationEntity) {
        final String sql = "UPDATE station SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, stationEntity.getName(), stationEntity.getId());
    }

    @Override
    public void deleteById(final Long id) {
        jdbcTemplate.update("DELETE FROM station WHERE id = ?", id);
    }

    public List<StationEntity> findById(final List<Long> ids) {
        final String sql = "SELECT * FROM station WHERE id IN (:ids)";
        final SqlParameterSource params = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcTemplate.query(sql, params, stationRowMapper);
    }

    public Optional<StationEntity> findByName(final String name) {
        final String sql = "SELECT * FROM station WHERE name = ?";
        return findInOptional(jdbcTemplate, sql, stationRowMapper, name);
    }
}
