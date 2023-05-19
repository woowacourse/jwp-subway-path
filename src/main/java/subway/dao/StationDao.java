package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;
import subway.exception.business.AlreadyExistStationException;
import subway.exception.business.StationNotFoundException;

import java.util.List;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public Station insert(Station station) throws AlreadyExistStationException {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        try {
            Long id = insertAction.executeAndReturnKey(params).longValue();
            return new Station(id, station.getName());
        } catch (Exception e) {
            throw new AlreadyExistStationException();
        }
    }

    public List<Station> findAll() {
        String sql = "select * from STATION";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findById(Long id) throws StationNotFoundException {
        String sql = "select * from STATION where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (Exception e) {
            throw new StationNotFoundException();
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
