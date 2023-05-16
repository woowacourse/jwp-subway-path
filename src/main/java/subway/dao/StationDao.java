package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;
import subway.domain.Station;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("STATIONS")
                .usingGeneratedKeyColumns("id");
    }

    private final RowMapper<StationEntity> rowMapper = (resultSet, rowNumber) ->
            new StationEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );


    public Long save(final String stationName) {
        final MapSqlParameterSource name = new MapSqlParameterSource().addValue("name", stationName);
        return insertAction.executeAndReturnKeyHolder(name).getKey().longValue();
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATIONS";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "select * from STATIONS where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findByName(final String upStationName) {
        final String sql = "SELECT id, name FROM STATIONS WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, upStationName));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "delete from STATIONS where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Long save(final StationEntity stationEntity) {
        final MapSqlParameterSource name = new MapSqlParameterSource()
                .addValue("name", stationEntity.getName());
        return insertAction.executeAndReturnKeyHolder(name).getKey().longValue();
    }
}
