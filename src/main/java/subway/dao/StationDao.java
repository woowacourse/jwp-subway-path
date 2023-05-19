package subway.dao;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import subway.domain.line.Station;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Station> stationMapper;

    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.stationMapper = (resultSet, rowNum) -> {
            return new Station(
                    resultSet.getLong("id"),
                    resultSet.getString("name"));
        };
    }

    public Station insert(Station station) {
        String sql = "INSERT INTO station(name) VALUES (?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, station.getName());
            return ps;
        }, keyHolder);
        long stationId = Objects.requireNonNull(keyHolder.getKey()).longValue();
        return new Station(stationId, station.getName());
    }

    public Optional<Station> findById(long stationId) {
        String sql = "SELECT * FROM station WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, stationMapper, stationId));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Station> findByName(String stationName) {
        String sql = "SELECT * FROM station WHERE name = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, stationMapper, stationName));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Station> findAll() {
        String sql = "SELECT * FROM station ORDER BY id";
        return jdbcTemplate.query(sql, stationMapper);
    }
}
