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

    private final RowMapper<Station> rowMapper = (rs, rowNum) ->
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
        final SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        try {
            final Long id = insertAction.executeAndReturnKey(params).longValue();

            return new Station(id, station.getName());
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("역 이름은 중복될 수 없습니다.");
        }
    }

    public List<Station> findAll() {
        final String sql = "SELECT * FROM station";

        return jdbcTemplate.query(sql, rowMapper);
    }

    public Station findById(final Long id) {
        final String sql = "SELECT * FROM station WHERE id = ?";

        try {
            return jdbcTemplate.queryForObject(sql, rowMapper, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new IllegalArgumentException("존재하지 않는 역입니다.");
        }
    }

    public void update(final Station station) {
        final String sql = "UPDATE station SET name = ? WHERE id = ?";

        jdbcTemplate.update(sql, station.getName(), station.getId());
    }

    public void deleteById(final Long id) {
        final String sql = "DELETE FROM station WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}
