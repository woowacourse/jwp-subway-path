package subway.persistence.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.persistence.entity.StationEntity;

@Repository
public class StationDao {

    private static final RowMapper<StationEntity> stationRowMapper = (rs, rowNum) -> new StationEntity(
            rs.getLong("id"),
            rs.getString("name")
    );

    private final JdbcTemplate template;
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public StationDao(final JdbcTemplate template) {
        this.template = template;
        this.simpleJdbcInsert = new SimpleJdbcInsert(template)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(template);
    }

    public Long save(final StationEntity stationEntity) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(stationEntity);
        return simpleJdbcInsert.executeAndReturnKey(source).longValue();
    }

    public Optional<StationEntity> findByName(final String name) {
        try {
            final String sql = "select * from station where station.name = ?";
            return Optional.ofNullable(template.queryForObject(sql, stationRowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<StationEntity> findAllByIds(final Set<Long> ids) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", ids);

        return namedParameterJdbcTemplate.query("SELECT * FROM station WHERE id IN (:ids)",
                parameters,
                stationRowMapper);
    }
}
