package subway.line.infrastructure.persistence.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.line.infrastructure.persistence.entity.StationEntity;

@Repository
public class StationDao {

    private static final RowMapper<StationEntity> stationRowMapper = (rs, rowNum) -> new StationEntity(
            UUID.fromString(rs.getString("domain_id")),
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

    public void save(final StationEntity stationEntity) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(stationEntity);
        simpleJdbcInsert.execute(source);
    }

    public Optional<StationEntity> findByName(final String name) {
        try {
            final String sql = "select * from station where station.name = ?";
            return Optional.ofNullable(template.queryForObject(sql, stationRowMapper, name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<StationEntity> findAllByDomainIds(final Set<UUID> domainIds) {
        final MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("ids", domainIds);
        return namedParameterJdbcTemplate.query("SELECT * FROM station WHERE domain_id IN (:ids)",
                parameters,
                stationRowMapper);
    }
}
