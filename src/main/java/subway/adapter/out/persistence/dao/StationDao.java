package subway.adapter.out.persistence.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.adapter.out.persistence.entity.StationEntity;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(StationEntity stationEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, stationEntity.getName());
    }

    public List<StationEntity> findAll() {
        String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "SELECT * FROM station WHERE id = ?";
        try {
            StationEntity station = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.of(station);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<StationEntity> findByName(final String name) {
        String sql = "SELECT * FROM station WHERE name = ?";
        try {
            StationEntity station = jdbcTemplate.queryForObject(sql, rowMapper, name);
            return Optional.of(station);
        } catch (IncorrectResultSizeDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(StationEntity newStationEntity) {
        String sql = "UPDATE station SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{newStationEntity.getName(), newStationEntity.getId()});
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM STATION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
