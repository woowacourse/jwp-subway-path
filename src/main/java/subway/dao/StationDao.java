package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Station insert(final Station station) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        final Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Station(id, station.getName());
    }

    public List<Station> findAll() {
        final String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findById(final Long id) {
        final String sql = "select * from STATION where id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public void update(final Station newStation) {
        final String sql = "update STATION set name = ? where id = ?";
        jdbcTemplate.update(sql, newStation.getName(), newStation.getId());
    }

    public void deleteById(final Long id) {
        final String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Optional<Station> findByName(final String name) {
        final String sql = "select id, name from STATION where name = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, name));
    }

    public List<Station> findAllById(final List<Long> stationIds) {
        final String sql = "select id, name from STATION where id IN (:id)";
        final MapSqlParameterSource source = new MapSqlParameterSource("id", stationIds);
        return namedParameterJdbcTemplate.query(sql, source, rowMapper);
    }
}
