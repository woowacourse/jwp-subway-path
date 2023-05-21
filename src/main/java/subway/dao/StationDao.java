package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

@Component
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
        final Long id = this.insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, stationEntity.getName());
    }

    public List<StationEntity> findAll() {
        final String sql = "select * from STATION";
        return this.jdbcTemplate.query(sql, this.rowMapper);
    }

    public StationEntity findById(final Long id) {
        final String sql = "select * from STATION where id = ?";
        return this.jdbcTemplate.queryForObject(sql, this.rowMapper, id);
    }

    public void update(final StationEntity newStationEntity) {
        final String sql = "update STATION set name = ? where id = ?";
        this.jdbcTemplate.update(sql, newStationEntity.getName(), newStationEntity.getId());
    }

    public void deleteById(final Long id) {
        final String sql = "delete from STATION where id = ?";
        this.jdbcTemplate.update(sql, id);
    }
}
