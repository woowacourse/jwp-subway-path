package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
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
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public Long insert(final StationEntity stationEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<StationEntity> findAll() {
        final String sql = "select * from station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(final Long id) {
        final String sql = "select * from station where id = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public List<StationEntity> findById(final List<Long> ids) {
        final String sql = "select * from station where id in (:ids)";
        final SqlParameterSource params = new MapSqlParameterSource("ids", ids);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    public Optional<StationEntity> findByName(final String name) {
        final String sql = "select * from station where name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
