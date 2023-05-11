package subway.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

@Repository
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("station")
                .usingGeneratedKeyColumns("id")
                .usingColumns("name");
    }

    public StationEntity save(final StationEntity stationEntity) {
        final SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(stationEntity);
        final Long stationId = jdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new StationEntity(stationId, stationEntity.getName());
    }

    public Optional<StationEntity> findById(final Long stationId) {
        final String sql = "SELECT id, name FROM station WHERE id = ?";
        try {
            final StationEntity result = jdbcTemplate.queryForObject(
                    sql,
                    (rs, rowNum) -> new StationEntity(
                            rs.getLong("id"),
                            rs.getString("name")
                    ),
                    stationId);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }
}
