package subway.station.out;

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

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(StationEntity stationEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, stationEntity.getName());
    }

    public List<StationEntity> findAll() {
        final String sql = "select id, name from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findByName(String name) {
        final String sql = "select id, name from STATION where name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, optionalRowMapper, name);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findById(Long id) {
        final String sql = "select id, name from STATION where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, optionalRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(StationEntity entity) {
        final String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, entity.getName(), entity.getId());
    }

    public void deleteById(long id) {
        final String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
