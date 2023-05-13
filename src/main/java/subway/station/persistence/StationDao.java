package subway.station.persistence;

import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.station.domain.Station;

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
            .usingColumns("name")
            .usingGeneratedKeyColumns("id");
    }

    public Long insert(final Station station) {
        return simpleJdbcInsert.executeAndReturnKey(new BeanPropertySqlParameterSource(station))
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

    public StationEntity findByName(final String name) {
        final String sql = "SELECT * FROM STATION WHERE name = :name";
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("name", name);

        final List<StationEntity> findStation =
            namedParameterJdbcTemplate.query(sql, params, STATION_ENTITY_ROW_MAPPER);

        if (findStation.isEmpty()) {
            throw new IllegalArgumentException("해당 이름의 역이 존재하지 않습니다.");
        }
        return findStation.get(0);
    }
}
