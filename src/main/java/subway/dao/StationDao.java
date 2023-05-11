package subway.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.exceptions.customexceptions.InvalidDataException;
import subway.exceptions.customexceptions.NotFoundException;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
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
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("이미 존재하는 역입니다.");
        }
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findById(Long id) {
        String sql = "select * from STATION where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (DataAccessException e) {
            throw new NotFoundException("해당하는 역이 존재하지 않습니다.");
        }
    }

    public Station findByName(String name) {
        String sql = "select * from STATION where name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, name);
        } catch (DataAccessException e) {
            throw new NotFoundException("해당하는 역이 존재하지 않습니다.");
        }
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
