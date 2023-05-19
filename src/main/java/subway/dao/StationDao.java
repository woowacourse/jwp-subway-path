package subway.dao;

import java.util.List;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import subway.domain.Station;
import subway.exception.DatabaseException;
import subway.exception.ExceptionType;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
        new Station(
            rs.getLong("id"),
            rs.getString("name")
        );

    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("station")
            .usingGeneratedKeyColumns("id");
    }

    public Station insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        try {
            Long id = insertAction.executeAndReturnKey(params).longValue();
            return new Station(id, station.getName());
        } catch(DataIntegrityViolationException exception) {
            throw new DatabaseException(ExceptionType.STATION_NAME_IS_ALREADY_EXISTED);
        }
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Station> findById(Long id) {
        try {
            String sql = "select * from STATION where id = ?";
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch(EmptyResultDataAccessException ex) {
            return Optional.empty();
        }

    }

    public int update(Station newStation) {
        String sql = "update STATION set name = ? where id = ?";
        return jdbcTemplate.update(sql, newStation.getName(), newStation.getId());
    }

    public int deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        return jdbcTemplate.update(sql, id);
    }
}
