package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.Station;

import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
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

    public Long insert(Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public Optional<Station> findById(Long id) {
        String sql = "select * from STATION where id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String sql = "delete from STATION where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Station> findAll() {
        String sql = "select * from station";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findByName(String name) {
        String sql = "select * from station where name = ?";

        return jdbcTemplate.queryForObject(sql,rowMapper,name);
    }
}
