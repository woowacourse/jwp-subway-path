package subway.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.dao.entity.StationEntity;
import subway.exception.DuplicatedException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class StationDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            new StationEntity(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(NamedParameterJdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(StationEntity station) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(station);
        try {
            Long id = insertAction.executeAndReturnKey(params).longValue();
            return new StationEntity(id, station.getName());
        } catch (DuplicateKeyException exception) {
            throw new DuplicatedException("이미 존재하는 역입니다.");
        }
    }

    public List<StationEntity> findAll() {
        String sql = "SELECT * FROM station";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<StationEntity> findById(Long id) {
        String sql = "SELECT * from station WHERE id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, source, rowMapper));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(StationEntity newStation) {
        String sql = "UPDATE station SET name = :name WHERE id = :id";
        SqlParameterSource source = new BeanPropertySqlParameterSource(newStation);
        jdbcTemplate.update(sql, source);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM station WHERE id = :id";
        SqlParameterSource source = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, source);
    }

    public Optional<StationEntity> findByName(String name) {
        String sql = "SELECT * FROM station WHERE name = :name";
        SqlParameterSource source = new MapSqlParameterSource("name", name);
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, source, rowMapper));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }
}
