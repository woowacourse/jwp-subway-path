package subway.adapter.subway.out.station;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
        new StationEntity(
            rs.getLong("id"),
            rs.getString("name")
        );
    private final RowMapper<Optional<StationEntity>> optionalRowMapper = (rs, rowNum) ->
        Optional.of(new StationEntity(
            rs.getLong("id"),
            rs.getString("name")
        ));

    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("station")
            .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(final StationEntity stationEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        final Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, stationEntity.getName());
    }

    public List<StationEntity> findAll() {
        final String sql = "select id,name from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findByName(final String name) {
        final String sql = "select id,name from STATION where name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, optionalRowMapper, name);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findById(final Long id) {
        final String sql = "select id,name from STATION where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, optionalRowMapper, id);
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final StationEntity entity) {
        final String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, entity.getName(), entity.getId());
    }

    public void deleteById(final long id) {
        final String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
