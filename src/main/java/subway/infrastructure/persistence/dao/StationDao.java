package subway.infrastructure.persistence.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.infrastructure.persistence.entity.StationEntity;

@Repository
public class StationDao {

    private static final RowMapper<StationEntity> stationRowMapper = (rs, rowNum) -> new StationEntity(
            rs.getLong("id"),
            rs.getString("name")
    );

    private final JdbcTemplate template;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public StationDao(final JdbcTemplate template) {
        this.template = template;
        this.simpleJdbcInsert = new SimpleJdbcInsert(template)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(final StationEntity stationEntity) {
        final SqlParameterSource source = new BeanPropertySqlParameterSource(stationEntity);
        return simpleJdbcInsert.executeAndReturnKey(source)
                .longValue();
    }

    public Optional<StationEntity> findById(final Long id) {
        try {
            final String sql = "select * from station where station.id = ?";
            return Optional.ofNullable(template.queryForObject(sql, stationRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
