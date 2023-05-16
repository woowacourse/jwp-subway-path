package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

@Repository
public class StationDao {

    private static final RowMapper<Station> STATION_ROW_MAPPER = (rs, rowNum) ->
        Station.of(
            rs.getLong("id"),
            rs.getString("name")
        );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("station")
            .usingGeneratedKeyColumns("id");
    }

    public Station insert(final Station station) {
        System.out.println("station.getName() = " + station.getName());
        final Long savedId = insertAction.executeAndReturnKey(new BeanPropertySqlParameterSource(station))
            .longValue();
        return Station.of(savedId, station.getName());
    }

    public List<Station> findAll() {
        final String sql = "SELECT * FROM STATION";
        return jdbcTemplate.query(sql, STATION_ROW_MAPPER);
    }

    public Optional<Station> findByName(final String name) {
        final String sql = "SELECT * FROM STATION WHERE name = ?";
        try {
            final Station station = jdbcTemplate.queryForObject(sql, STATION_ROW_MAPPER, name);
            return Optional.of(station);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Station findById(Long id) {
        String sql = "select * from STATION where id = ?";
        return jdbcTemplate.queryForObject(sql, STATION_ROW_MAPPER, id);
    }

    public void update(Station newStation) {
        String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newStation.getName(), newStation.getId()});
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }
}
