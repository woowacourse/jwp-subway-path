package subway.persistence.dao;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.persistence.entity.StationEntity;

@Component
public class StationDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) ->
            StationEntity.of(
                    rs.getLong("id"),
                    rs.getString("name")
            );


    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(final StationEntity stationEntity) {
        final SqlParameterSource params = new BeanPropertySqlParameterSource(stationEntity);
        final Long id = insertAction.executeAndReturnKey(params).longValue();

        return new StationEntity(id, stationEntity.getName());
    }

    public Optional<StationEntity> findById(final Long id) {
        final String sql = "SELECT id, name FROM station WHERE id = :id";
        final MapSqlParameterSource parameter = new MapSqlParameterSource("id", id);

        return namedParameterJdbcTemplate.query(sql, parameter, rowMapper)
                .stream()
                .findAny();
    }

    public boolean existsByName(final String name) {
        final String sql = "SELECT id, name FROM station WHERE name = :name";
        final MapSqlParameterSource parameter = new MapSqlParameterSource("name", name);

        return namedParameterJdbcTemplate.query(sql, parameter, rowMapper)
                .stream()
                .findAny()
                .isPresent();
    }

    public List<StationEntity> findAllByIds(final Set<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }

        final String sql = "SELECT id, name FROM station WHERE id IN (:ids)";
        final SqlParameterSource parameters = new MapSqlParameterSource("ids", ids);

        return namedParameterJdbcTemplate.query(sql, parameters, rowMapper);
    }

    public int deleteById(final Long id) {
        final String sql = "DELETE FROM station WHERE id = :id";
        final MapSqlParameterSource parameter = new MapSqlParameterSource("id", id);

        return namedParameterJdbcTemplate.update(sql, parameter);
    }
}
