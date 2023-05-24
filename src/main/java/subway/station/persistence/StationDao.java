package subway.station.persistence;

import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class StationDao {

    private static final RowMapper<StationEntity> STATION_ENTITY_ROW_MAPPER = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String stationName = rs.getString("station_name");
        return new StationEntity(id, stationName);
    };

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public StationDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
            .withTableName("STATION")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(final StationEntity stationEntity) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(stationEntity))
            .longValue();
    }

    public Optional<StationEntity> findById(final Long stationId) {
        final String sql = "SELECT * FROM STATION WHERE id = ?";

        return jdbcTemplate.query(sql, STATION_ENTITY_ROW_MAPPER, stationId)
                .stream().findAny();
    }
}
