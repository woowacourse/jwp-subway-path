package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> stationEntityRowMapper = (rs, rowNum) ->
            new StationEntity.Builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name"))
                    .build();

    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(StationEntity stationEntity) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        long id = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity.Builder()
                .id(id)
                .name(stationEntity.getName())
                .build();
    }

    public List<StationEntity> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, stationEntityRowMapper);
    }

    public Optional<StationEntity> findById(long id) {
        try {
            String sql = "select * from STATION where id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, stationEntityRowMapper, id));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public Optional<Long> findIdByName(String name) {
        try {
            String sql = "select id from STATION where name = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, Long.class, name));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
