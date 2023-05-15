package subway.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.Map;

@Repository
public class StationH2Dao implements StationDao {

    public static final RowMapper<StationEntity> STATION_ENTITY_ROW_MAPPER = (resultSet, rowNum) ->
            new StationEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertStation;

    public StationH2Dao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertStation = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public StationEntity insert(final StationEntity stationEntity) {
        final Map<String, String> parameter = Map.of("name", stationEntity.getName());
        final long id = insertStation.executeAndReturnKey(parameter).longValue();
        return new StationEntity(id, stationEntity.getName());
    }

    @Override
    public StationEntity findBy(final Long id) {
        final String sql = "SELECT * FROM station WHERE id = :id";
        final Map<String, Long> parameter = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, parameter, STATION_ENTITY_ROW_MAPPER);
    }

    @Override
    public StationEntity findBy(final String name) {
        final String sql = "SELECT * FROM station WHERE name = :name";
        final Map<String, String> parameter = Map.of("name", name);
        return namedParameterJdbcTemplate.queryForObject(sql, parameter, STATION_ENTITY_ROW_MAPPER);
    }
}
