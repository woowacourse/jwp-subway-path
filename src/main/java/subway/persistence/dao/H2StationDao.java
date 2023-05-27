package subway.persistence.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.Entity.StationEntity;
import subway.domain.station.Station;
import subway.persistence.NullChecker;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

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
    public StationEntity insert(Station station) {
        NullChecker.isNull(station);
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(id, station.getName());
    }

    @Override
    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<StationEntity> findById(Long id) {
        NullChecker.isNull(id);
        String sql = "select * from STATION where id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Long> findIdByName(String name) {
        NullChecker.isNull(name);
        String sql = "select id from STATION where name = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, Long.class, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public void update(StationEntity stationEntity) {
        NullChecker.isNull(stationEntity);
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{stationEntity.getName(), stationEntity.getId()});
    }

    @Override
    public void deleteById(Long id) {
        NullChecker.isNull(id);
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
