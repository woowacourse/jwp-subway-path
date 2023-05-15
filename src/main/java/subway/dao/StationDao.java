package subway.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
        new StationEntity(
            rs.getLong("id"),
            rs.getString("name")
        );


    public StationDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("station")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(final StationEntity station) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public List<StationEntity> findAll() {
        final String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        final String sql = "SELECT * FROM station WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (final EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public int update(final StationEntity newStation) {
        final String sql = "UPDATE station SET name = ? WHERE id = ?";
        return jdbcTemplate.update(sql, newStation.getName(), newStation.getId());
    }

    public int deleteById(final Long id) {
        final String sql = "DELETE FROM station WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

    public boolean existByName(final String name) {
        final String sql = "SELECT COUNT(*) FROM station WHERE name = ?";
        final long count = jdbcTemplate.queryForObject(sql, Long.class, name);
        return count > 0;
    }
}
