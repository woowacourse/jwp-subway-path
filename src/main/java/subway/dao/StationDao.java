package subway.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import subway.entity.StationEntity;

@Component
public class StationDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<StationEntity> rowMapper = (rs, rowNum) -> new StationEntity(
        rs.getLong("id"),
        rs.getString("name"),
        rs.getLong("line_id")
    );

    public StationDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("station")
            .usingGeneratedKeyColumns("id");
    }

    public StationEntity insert(StationEntity station) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", station.getName());
        params.put("line_id", station.getLineId());

        Long stationId = insertAction.executeAndReturnKey(params).longValue();
        return new StationEntity(stationId, station.getName(), station.getLineId());
    }

    public List<StationEntity> insertInit(List<StationEntity> stationEntities) {
        List<StationEntity> stations = new ArrayList<>();
        for (StationEntity station : stationEntities) {
            Map<String, Object> params = new HashMap<>();

            params.put("name", station.getName());
            params.put("line_id", station.getLineId());

            Long stationId = insertAction.executeAndReturnKey(params).longValue();

            stations.add(new StationEntity(stationId, station.getName(), station.getLineId()));
        }
        return stations;
    }

    public Optional<StationEntity> findById(final Long id) {
        String sql = "SELECT id, name, line_id FROM STATION WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
