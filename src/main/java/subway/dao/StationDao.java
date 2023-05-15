package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.station.Station;

import javax.sql.DataSource;
import java.util.Optional;

@Component
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    final RowMapper<Station> stationRowMapper = (result, count) ->
            new Station(result.getLong("id"),
                    result.getString("name")
            );

    public Station insert(final String stationName) {
        final SqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", stationName);
        final Long id = jdbcInsert.executeAndReturnKey(params).longValue();
        return new Station(id, stationName);
    }

    public Optional<Station> findById(final Long stationId) {
        final String sql = "SELECT s.id, s.name FROM station s WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, stationRowMapper, stationId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Station> findByName(final String name) {
        final String sql = "SELECT s.id, s.name FROM station s WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, stationRowMapper, name));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}