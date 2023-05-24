package subway.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.StationEntity;

import java.util.Map;
import java.util.Optional;

@Repository
public class StationH2Dao implements StationDao {

    public static final RowMapper<StationEntity> STATION_ENTITY_ROW_MAPPER = (resultSet, rowNum) ->
            new StationEntity(
                    resultSet.getLong("id"),
                    resultSet.getString("name")
            );
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert insertStation;

    public StationH2Dao(final JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.insertStation = new SimpleJdbcInsert(jdbcTemplate)
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
    public Optional<StationEntity> findBy(final Long id) {
        final String sql = "SELECT * FROM station WHERE id = :id";
        final Map<String, Long> parameter = Map.of("id", id);
        try {
            return Optional.ofNullable(namedParameterJdbcTemplate.queryForObject(sql, parameter, STATION_ENTITY_ROW_MAPPER));
        } catch (EmptyResultDataAccessException exceptionn) {
            return Optional.empty();
        }
    }
}
