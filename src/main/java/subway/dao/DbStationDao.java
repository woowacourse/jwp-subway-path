package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Repository
public class DbStationDao implements StationDao {

    public static final RowMapper<Station> STATION_ROW_MAPPER = (resultSet, rowNum) -> new Station(
            resultSet.getLong("id"),
            resultSet.getString("name")
    );
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertStation;

    public DbStationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertStation = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Station saveStation(Station station) {
        SqlParameterSource parameters = new BeanPropertySqlParameterSource(station);
        final long newId = insertStation.executeAndReturnKey(parameters).longValue();
        return new Station(newId, station.getName());
    }

    @Override
    public List<Station> findAll() {
        final String sql = "select * from STATION";
        return jdbcTemplate.query(sql, STATION_ROW_MAPPER);
    }

    @Override
    public Station findById(final Long id) {
        final String sql = "select * from STATION WHERE id = :id";
        final Map<String, Long> parameter = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameter, STATION_ROW_MAPPER);
    }
}

