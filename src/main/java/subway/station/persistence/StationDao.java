package subway.station.persistence;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class StationDao {

    private static final RowMapper<StationEntity> STATION_ENTITY_ROW_MAPPER = (rs, rowNum) -> {
        final Long id = rs.getLong("id");
        final String stationName = rs.getString("name");
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

    public Long findIdByName(final String name) {
        final String sql = "SELECT * FROM STATION WHERE name = ?";
        final List<Long> findId = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getLong("id"), name);
        if (findId.isEmpty()) {
            throw new IllegalArgumentException("해당 이름의 역이 존재하지 않습니다.");
        }

        return findId.get(0);
    }

    public Optional<StationEntity> findById(final Long stationId) {
        final String sql = "SELECT * FROM STATION WHERE id = :stationId";
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", stationId);

        return namedParameterJdbcTemplate.query(sql, params, STATION_ENTITY_ROW_MAPPER)
                .stream().findAny();
    }
}
