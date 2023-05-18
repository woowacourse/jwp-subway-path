package subway.domain.station.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.station.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(final StationEntity stationEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        final Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, stationEntity.getName());
    }

    public List<StationEntity> findAll() {
        final String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public StationEntity findById(final Long id) {
        final String sql = "select * from STATION where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public List<StationEntity> findByIds(List<Long> ids) {
        final String sql = "select * from STATION where id in (?)";
        String joinedIds = ids.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        return jdbcTemplate.query(sql.replace("?", joinedIds), rowMapper);
    }

    public Optional<StationEntity> findByName(final String name) {
        try {
            final String sql = "select * from STATION where name = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(final StationEntity newStationEntity) {
        final String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newStationEntity.getName(), newStationEntity.getId()});
    }

    public void deleteById(final Long id) {
        final String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
