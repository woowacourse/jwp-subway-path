package subway.adapter.out.persistence.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.adapter.out.persistence.entity.StationEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class StationJdbcDao implements StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    private final RowMapper<StationEntity> stationRowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationJdbcDao(final JdbcTemplate jdbcTemplate) {
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Long createStation(final StationEntity stationEntity) {
        final Map<String, Object> parameters = new HashMap<>();

        parameters.put("name", stationEntity.getName());

        return insert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<StationEntity> findAll() {
        String sql = "select * from station";
        return jdbcTemplate.query(sql, stationRowMapper);
    }

    @Override
    public void deleteById(final Long stationIdRequest) {
        String sql = "delete from station where id = ?";
        jdbcTemplate.update(sql, stationIdRequest);
    }
}
