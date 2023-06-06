package subway.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.subwaymap.Station;

@Component
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
            .withTableName("STATION")
            .usingGeneratedKeyColumns("id");
    }

    public Station insert(final Station station) {
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
        final String sql = "SELECT * FROM STATION WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, STATION_ROW_MAPPER, id);
    }

    public void update(Station newStation) {
        final String sql = "UPDATE STATION SET name = ? WHERE id = ?";
        jdbcTemplate.update(sql, new Object[]{newStation.getName(), newStation.getId()});
    }

    public void deleteById(Long id) {
        final String sql = "DELETE FROM STATION WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
