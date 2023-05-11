package subway.persistence;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.domain.repository.StationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StationRepositoryImpl implements StationRepository {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insert;

    private final RowMapper<Station> stationRowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationRepositoryImpl(final JdbcTemplate jdbcTemplate) {
        this.insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createStation(final Station station) {
        final Map<String, Object> parameters = new HashMap<>();

        parameters.put("name", station.getName());

        insert.execute(parameters);
    }

    @Override
    public List<Station> findAll() {
        String sql = "select * from station";
        return jdbcTemplate.query(sql, stationRowMapper);
    }

    @Override
    public Station findById(final Long stationIdRequest) {
        String sql = "select * from station where id = ?";
        return jdbcTemplate.queryForObject(sql, stationRowMapper, stationIdRequest);
    }

    @Override
    public void deleteById(final Long stationIdRequest) {
        String sql = "delete from station where id = ?";
        jdbcTemplate.update(sql, stationIdRequest);
    }
}
