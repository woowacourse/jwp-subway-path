package subway.dao;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.domain.Station;

import java.util.List;

@Component
public class StationDao {
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Station> rowMapper = (rs, rowNum) ->
            new Station(
                    rs.getLong("id"),
                    rs.getString("name")
            );

    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public Station insert(final Station station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        try {
            final Long id = insertAction.executeAndReturnKey(params).longValue();

            return new Station(id, station.getName());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("역 이름은 중복될 수 없습니다.");
        }
    }

    public List<Station> findAll() {
        final String sql = "select * from STATION";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findById(final Long id) {
        final String sql = "select * from STATION where id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
    }

    public void update(final Station newStation) {
        final String sql = "update STATION set name = ? where id = ?";

        jdbcTemplate.update(sql, new Object[]{newStation.getName(), newStation.getId()});
    }

    public void deleteById(final Long id) {
        final String sql = "delete from STATION where id = ?";

        jdbcTemplate.update(sql, id);
    }
}
