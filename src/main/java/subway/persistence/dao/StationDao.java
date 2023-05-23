package subway.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.StationEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Component
public class StationDao {

    private static final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public long insert(final StationEntity station) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<StationEntity> findAll() {
        final String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public StationEntity findById(final Long id) {
        final String sql = "select * from STATION where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(final StationEntity newStation) {
        final String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, newStation.getName(), newStation.getId());
    }

    public void deleteById(final Long id) {
        final String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<StationEntity> findByName(final String name) {
        final String sql = "select id, name from STATION where name = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, name));
    }

    public List<StationEntity> findAllById(final List<Long> stationIds) {
        if (stationIds.isEmpty()) {
            return new LinkedList<>();
        }

        final String sql = "select id, name from STATION where id IN (:id)";
        final MapSqlParameterSource source = new MapSqlParameterSource("id", stationIds);
        return namedParameterJdbcTemplate.query(sql, source, rowMapper);
    }
}
