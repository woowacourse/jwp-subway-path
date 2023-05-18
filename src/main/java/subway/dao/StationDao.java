package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;
import subway.exception.StationNotFoundException;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StationDao {

    public static final RowMapper<StationEntity> stationEntityRowMapper = (rs, rn) -> new StationEntity(
            rs.getLong("id"),
            rs.getString("name")
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public Long insert(StationEntity stationEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<StationEntity> findById(Long id) {
        String sql = "select * from STATION where id = ?";
        return jdbcTemplate.query(sql, stationEntityRowMapper, id);
    }

    public List<StationEntity> findByName(final String name) {
        String sql = "SELECT * FROM station WHERE name = ?";
        return jdbcTemplate.query(sql, stationEntityRowMapper, name);
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, stationEntityRowMapper);
    }

    public int update(StationEntity newStationEntity) {
        String sql = "update STATION set name = ? where id = ?";
        return jdbcTemplate.update(sql, new Object[]{newStationEntity.getName(), newStationEntity.getId()});
    }

    public int deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
