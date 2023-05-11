package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class H2StationDao implements StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public H2StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public StationEntity insert(StationEntity stationEntity) {
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
    public StationEntity findById(Long id) {
        String sql = "SELECT * FROM station WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    @Override
    public void update(StationEntity newStationEntity) {
        String sql = "UPDATE station SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, newStationEntity.getName(), newStationEntity.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM STATION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
