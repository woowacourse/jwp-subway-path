package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DbStationDao implements StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertStation;
    public final RowMapper<StationEntity> rowMapper = (resultSet, rowNum) -> new StationEntity(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );

    public DbStationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertStation = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public StationEntity saveStation(StationEntity stationEntity) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", stationEntity.getName());

        final long id = insertStation.executeAndReturnKey(parameters).longValue();
        return new StationEntity(id, stationEntity.getName());
    }

    @Override
    public List<StationEntity> findAll() {
        final String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public StationEntity findById(final Long id) {
        final String sql = "select * from STATION WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public Optional<StationEntity> findByName(String name) {
        final String sql = "select * from STATION WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void delete(Long id) {
        final String sql = "delete from STATION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}

